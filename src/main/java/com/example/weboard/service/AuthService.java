package com.example.weboard.service;

import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.TokensDTO;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.exception.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Expr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RedisService redisService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-expiration}")
    private int accessJWTExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private int refreshJWTExpirationMs;

    /**
     * JWT 토큰에서 사용자 ID를 추출하여 주어진 ID와 비교합니다.
     * @param id 사용자 ID
     * @param JWT 검증할 JWT 토큰
     * @return JWT 토큰의 ID와 주어진 ID가 일치하면 true, 그렇지 않으면 false를 반환합니다.
     */
    public Boolean compareJwtToId(int id, String JWT){
        int idFromJwt = getIdFromToken(JWT);
        return idFromJwt == id;
    }

    /**
     * 사용자 로그인을 처리하고, JWT 토큰을 발급합니다.
     * @param userId 사용자 ID
     * @param password 사용자 비밀번호
     * @return 발급된 액세스 토큰과 리프레시 토큰을 포함한 TokensDTO 객체
     * @throws Exception 로그인 처리 중 발생하는 예외를 던집니다.
     */
    public TokensDTO loginAndJwtProvide(String userId, String password) throws Exception { //순서에 영향을 받음 PARAM을 .. 재사용성이 있으면 parameter을 써라
        UserDTO user = userService.getUserByIdOrUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        int id=user.getId();

        String storedPassword = user.getPassword();
        String hashedPassword = userService.plainToSha256(password);

        boolean valid = checkLastLoginAndLoginTrialMoreThan5(id);

        if (!storedPassword.equals(hashedPassword)) {
            int failCount = userService.addLoginFailCount(user);
            if(failCount>=5){ // 로그인 실패횟수가 5회 이상
                userService.updateLoginLock(user); // loginLock 시간 update
            }
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        // 로그인 성공, 로그인 실패 횟수 및 로그인 락 해제
        userService.resetLoginFailCountAndLoginLocked(user);

        //마지막 비번 변경 확인
        Date currentDate = new Date();
        long lastPwUpdatedFromToday = (currentDate.getTime() - user.getLastPwUpdated().getTime()) / (1000 * 60 * 60 * 24);
        // 3개월 이상 비번 변경 x
        if(lastPwUpdatedFromToday > 63) throw new LastPwException();

        //계정 잠금 확인
        // 계정이 잠겨 있음.
        if(user.getIsLocked()==1) throw new UserIsLockedException();

        TokensDTO IssuedTokens = new TokensDTO();
        IssuedTokens.setAccessToken(generateAccessJWT(user));
        IssuedTokens.setRefreshToken(generateRefreshJWT(user));
//        return generateAccessJWT(user);
        redisService.setValues(IssuedTokens.getAccessToken(), IssuedTokens.getRefreshToken());
        return IssuedTokens;
    }

    /**
     * 액세스 JWT의 유효성을 검증합니다.
     * @param accessJWT 검증할 액세스 JWT
     * @throws MalformedJwtException JWT 형식이 잘못되었을 때 예외를 던집니다.
     */
    public void checkJWTValid(String accessJWT){
        if(accessJWT == null || !accessJWT.startsWith("Bearer ") ) { // 7자 이상 조건도 만족
            throw new MalformedJwtException("dd");
        }
        String jwtToken = accessJWT.substring(7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwtToken);

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        }
    }

    /**
     * 액세스 JWT와 리프레시 JWT의 유효성을 검증하고, 필요에 따라 액세스 JWT를 재발급합니다.
     * @param accessJWT 검증할 액세스 JWT
     * @param refreshJWT 검증할 리프레시 JWT
     * @return 새로운 액세스 JWT를 반환합니다.
     * @throws RuntimeException JWT 검증 실패 시 예외를 던집니다.
     */
    public String checkJWTValid(String accessJWT, String refreshJWT) throws RuntimeException {
        if(accessJWT == null || !accessJWT.startsWith("Bearer ")) { // 7자 이상 조건도 만족
            throw new MalformedJwtException("");
        }
        accessJWT = accessJWT.substring(7);
        Jws<Claims> accessClaims;
//        refreshJWT = refreshJWT.substring(7);
        int id=0;
        StringBuilder userId = new StringBuilder();
        try {
            extractJWTClaims(accessJWT);
            // refresh token 유효성 확인 -> access token 재발급

        } catch (ExpiredJwtException e) {
            try {
                if((redisService.getValues(accessJWT)).equals(refreshJWT)){

                    extractJWTClaims(refreshJWT);
                    id = Integer.parseInt(e.getClaims().getSubject());
                    userId.append(e.getClaims().get("userId", String.class));
                }else{
                    throw new MalformedJwtException("access token과 refresh token이 일치하지 않습니다.");
                }
            } catch (ExpiredJwtException e1) {
                 // 예외 메시지 추가
                throw new MalformedJwtException("refresh token도 만료되었습니다. 다시 로그인 해주세요.");
            } catch (ClaimJwtException e3) {
                throw new MalformedJwtException("claim jwt exception");
            }
            // refresh token 유효성 확인 -> access token 재발급
            redisService.deleteValues(accessJWT);
            String newAccessJWT = generateAccessJWT(id, String.valueOf(userId)); // 새로운 Access JWT 발급
            redisService.setValues(newAccessJWT, refreshJWT);
            return newAccessJWT;
        }
        return "";
    }
    /**
     * 사용자 ID를 기반으로 액세스 JWT를 생성합니다.
     * @param id 사용자 ID
     * @param userId 사용자의 유저 ID
     * @return 생성된 액세스 JWT
     */
    private String generateAccessJWT(UserDTO user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessJWTExpirationMs);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .claim("userId", user.getUserId())
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    private String generateAccessJWT(int id, String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessJWTExpirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("userId", userId)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    /**
     * 사용자 정보를 기반으로 리프레시 JWT를 생성합니다.
     * @param user 사용자 정보
     * @return 생성된 리프레시 JWT
     */
    private String generateRefreshJWT(UserDTO user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshJWTExpirationMs);

        return Jwts.builder()
                .setSubject("weboard")
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * JWT에서 클레임을 추출합니다.
     * @param BearerJWT 분석할 Bearer JWT
     * @return 추출된 JWT 클레임
     */
    public Jws<Claims> extractJWTClaims(String BearerJWT){

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(BearerJWT);
    }

    /**
     * JWT에서 사용자 ID를 추출합니다.
     * @param JWT 검증할 JWT 토큰
     * @return 추출된 사용자 ID
     */
    public Integer getIdFromToken(String JWT) {
        if (JWT == null || !JWT.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없거나 유효하지 않습니다.");
        }
        String jwtToken = JWT.substring(7);
        try {
            return Integer.parseInt(extractJWTClaims(jwtToken).getBody().getSubject());
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new MalformedJwtException("토큰이 유효하지 않습니다.");
        }
    }

    /**
     * JWT 서명 키를 생성합니다.
     * JWT 서명에 사용될 비밀키를 생성하고 반환합니다.
     * @return 생성된 서명 키
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 사용자의 마지막 로그인 시도와 실패 횟수를 검사합니다.
     * 로그인이 5회 이상 실패했을 경우 로그인 잠금을 확인하고, 마지막 로그인 시점이 1개월을 초과했을 경우 사용자 계정을 잠급니다.
     * @param id 사용자의 ID
     * @return 로그인 잠금 및 마지막 로그인 검사가 문제없이 통과되면 true를 반환합니다.
     * @throws LoginLockException 로그인 실패로 인한 잠금 상황에서 발생하는 예외
     * @throws LastLoginException 마지막 로그인 시점이 1개월을 초과했을 때 발생하는 예외
     * @throws Exception 검사 과정 중 발생할 수 있는 기타 예외
     */
    public boolean checkLastLoginAndLoginTrialMoreThan5(int id) throws Exception{
        UserDTO user = userService.getUserByIdOrUserId(id);
        Date currentDate = new Date();

        //로그인 5회 이상 실패 확인
        if(user.getLoginLocked() !=null){
            long lastLoginLockFromNow = (currentDate.getTime() - user.getLoginLocked().getTime()) / (1000 * 60);
            // 5회 실패시 5분간 잠금
            if(user.getLoginFail()>=5 && lastLoginLockFromNow<5) throw new LoginLockException();
        }


        //마지막 로그인 확인
        // 마지막 로그인 날짜와 현재 날짜의 차이를 일 단위로 변환
        long lastLoginFromToday = (currentDate.getTime() - user.getLastLogin().getTime()) / (1000 * 60 * 60 * 24);
        if (lastLoginFromToday > 31) { // 1개월 이상 접속(로그인) x
            userService.lockUnlockUser(id, FrkConstants.lockUser);
            throw new LastLoginException();
        }

        return true;
    }




}
