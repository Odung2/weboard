package com.example.weboard.service;

import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.TokensDTO;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.exception.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
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

    @Value("${jwt.refresh-expiration")
    private int refreshJWTExpirationMs;

    public Boolean compareJwtToId(int id, String JWT){
        int idFromJwt = getIdFromToken(JWT);
        return idFromJwt == id;
    }
    public TokensDTO loginAndJwtProvide(String userId, String password) throws Exception { //순서에 영향을 받음 PARAM을 .. 재사용성이 있으면 parameter을 써라
        UserDTO user = userService.getUserByIdOrUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        int id=user.getId();

        String storedPassword = user.getPassword();
        String hashedPassword = userService.plainToSha256(password);

        boolean valid = checkLastLoginAndLastPwUpdatedAndLock(id);

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

    public void checkJWTValid(String JWT){
        if(JWT == null || !JWT.startsWith("Bearer ") ) { // 7자 이상 조건도 만족
            throw new MalformedJwtException("");
        }
    }
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
    private String generateRefreshJWT(UserDTO user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshJWTExpirationMs);

        return Jwts.builder()
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    public Integer getIdFromToken(String JWT) {
        if (JWT == null || !JWT.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없거나 유효하지 않습니다.");
        }
        String jwtToken = JWT.substring(7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwtToken);

            return Integer.parseInt(claims.getBody().getSubject());
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new MalformedJwtException("토큰이 유효하지 않습니다.");
        }
    }


    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean checkNewPwValid(String password) throws Exception{
        if(password.length()<8){
            throw new ShortPasswordException();
        }
        if(password.length()<12){
            if(!Pattern.matches(FrkConstants.passwordRegexUnder12, password)){
                throw new PasswordRegexException("12자 미만의 경우 영문 대문자, 소문자, 숫자, 특수문자의 조합으로 입력해주세요.");
            }
            return true;
        }
        if(!Pattern.matches(FrkConstants.passwordRegex12orMore, password)){
            throw new PasswordRegexException("12자 이상의 경우 영문 대/소문자, 숫자, 특수문자의 조합으로 입력해주세요.");
        }
        return true;
    }

    public boolean checkLastLoginAndLastPwUpdatedAndLock(int id) throws Exception{
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
