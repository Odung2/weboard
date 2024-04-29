package com.example.weboard.service;

import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.TokensDTO;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.exception.*;
import com.example.weboard.param.LoginParam;
import com.example.weboard.param.TokensParam;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Expr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

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
     * 사용자 로그인을 처리하고, JWT 토큰을 발급합니다.
     * @param loginParam userId & password
     * @return 발급된 액세스 토큰과 리프레시 토큰을 포함한 TokensDTO 객체
     * @throws Exception 로그인 처리 중 발생하는 예외를 던집니다.
     */
    public TokensDTO loginAndJwtProvide(LoginParam loginParam) throws Exception { //순서에 영향을 받음 PARAM을 .. 재사용성이 있으면 parameter을 써라
        UserDTO user = userService.getUser(loginParam.getUserId());
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        int id=user.getId();

        String storedPassword = user.getPassword();
        String hashedPassword = userService.plainToSha256(loginParam.getPassword());

        checkLastLoginAndLoginTrialMoreThan5(id); // 로그인 시도 제한되지 않았는지 확인

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
        IssuedTokens.setRefreshToken(generateRefreshJWT());
//        return generateAccessJWT(user);
        redisService.setValues(IssuedTokens.getAccessToken(), IssuedTokens.getRefreshToken());
        return IssuedTokens;
    }

    /**
     * 액세스 JWT의 유효성을 검증합니다.
     * @param accessJWT 검증할 액세스 JWT
     * @throws MalformedJwtException JWT 형식이 잘못되었을 때 예외를 던집니다.
     */
    public void checkAccessJWTValid(String accessJWT){
        if(accessJWT == null || !accessJWT.startsWith("Bearer ") ) { // 7자 이상 조건도 만족
            throw new MalformedJwtException("유효하지 않은 토큰 형식입니다.");
        }
        String jwtToken = accessJWT.substring(7);
        try {
            extractJWTClaims(jwtToken);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "access token이 만료되었습니다. weboard/users/refreshToken 에서 새로운 액세스 토큰을 발급받으세요.");
        }
    }

    /**
     * 액세스 JWT와 리프레시 JWT의 유효성을 검증하고, 필요에 따라 액세스 JWT를 재발급합니다.
     * @param accessJWT 검증할 액세스 JWT
     * @param refreshJWT 검증할 리프레시 JWT
     * @return 새로운 액세스 JWT를 반환합니다.
     * @throws RuntimeException JWT 검증 실패 시 예외를 던집니다.
     */
    public String checkRefreshJWTValid(String accessJWT, String refreshJWT) throws RuntimeException, TokenNotIssueException {

        if(StringUtils.isBlank(accessJWT) || !accessJWT.startsWith("Bearer ")) { // 7자 이상 조건도 만족
            throw new MalformedJwtException("유효하지 않은 토큰 형식입니다.");
        }

        //redis에 존재하는 refresh token이 있는지 검사
        if((redisService.getValues(accessJWT)).equals(refreshJWT)){
            try {
                extractJWTClaims(refreshJWT);
            } catch (ExpiredJwtException e1) {
                throw new ExpiredJwtException(e1.getHeader(), e1.getClaims(), "refresh token도 만료되었습니다. 다시 로그인 해주세요.");
            }
        }else{
            throw new NotFoundException("access token에 해당하는 refresh token이 존재하지 않습니다. 다시 로그인 해주세요.");
        }

        try {
            // access token 재발급 위해 access token의 개인 정보 필요
            extractJWTClaims(accessJWT);
        } catch (ExpiredJwtException e) {
            // access token은 만료되어야 새로 access token을 발급 해줌.(무한 발급 방지)
            int id = Integer.parseInt(e.getClaims().getSubject());
            String userId = e.getClaims().get("userId", String.class);

            redisService.deleteValues(accessJWT); // 기존 accessJWT(key), refresh(value) pair는 redis에서 삭제

            String newAccessJWT = generateAccessJWT(id, String.valueOf(userId)); // 새로운 Access JWT 발급
            redisService.setValues(newAccessJWT, refreshJWT); // redis에 새 조합 등록
            return newAccessJWT;
        }

        // 만약 access token이 만료되지 않은 경우, access token을 발급하지 않음.
        throw new TokenNotIssueException("액세스 토큰이 만료되지 않아 새 액세스 토큰을 발급할 수 없습니다.");
    }

    /**
     * 사용자 정보를 기반으로 액세스 JWT를 생성합니다.
     * @param user 사용자 정보
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
    /**
     * 사용자의 id, userId를 기반으로 액세스 JWT를 생성합니다.
     * @param id 사용자 ID
     * @param userId 사용자의 유저 ID
     * @return 생성된 액세스 JWT
     */
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
     * 리프레시 JWT를 생성합니다. 리프레시 토큰에는 개인정보가 없습니다.
     * @return 생성된 리프레시 JWT
     */
    private String generateRefreshJWT() {
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
     * JWT에서 클레임을 추출합니다.(for 유효성 검사)
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
     * JWT에서 사용자의 id를 추출합니다.
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
            throw new RuntimeException("액세스 토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("액세스 토큰이 유효하지 않습니다.");
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
        UserDTO user = userService.getUser(id);
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
