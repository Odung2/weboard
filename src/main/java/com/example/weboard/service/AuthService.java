package com.example.weboard.service;

import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.TokensDTO;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.exception.*;
import com.example.weboard.param.LoginParam;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.security.auth.login.CredentialException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static com.example.weboard.dto.FrkConstants.lockUser;
import static com.example.weboard.dto.FrkConstants.month;

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
     * 로그인(credential 확인) 및 [액세스 토큰, 리프레시 토큰] 발급
     * @param loginParam [userId, password]
     * @return issueTokens[accessJWT, refreshJWT]
     * @throws Exception
     */
    public TokensDTO loginAndIssueTokens(LoginParam loginParam) throws Exception { //순서에 영향을 받음 PARAM을 .. 재사용성이 있으면 parameter을 써라

        // DB에 저장된 유저 정보 존재 확인
        UserDTO user = userService.validateUser(loginParam.getUserId());
        // 로그인 시도 제한 확인
        validateLoginAttempts(user);
        // 비밀번호 확인 - 실제 로그인
        validatePassword(user, loginParam.getPassword());
        // 계정 잠금 확인
        validateUserLocked(user);
        // 로그인 1개월 이상 지났는지 -> 지났다면 계정 잠금
        validateLastLogin(user);
        // 토큰s 발급
        TokensDTO issueTokens = issueTokens(user);
        // 3개월 이상 비번 변경 x -> 비번 변경 에러 + 토큰s 발급
        validateLastPwUpdate(user, issueTokens);

        return issueTokens;
    }


    /**
     * DB에 저장된 유저의 비밀번호 일치 확인, 실패 시 로그인 실패 횟수 +1
     * @param user // DB에 저장된 유저 정보
     * @param loginPassword // 로그인 시 입력한 비밀번호
     * @throws CredentialException // 비밀번호 불일치 시 예외처리
     * @throws NoSuchAlgorithmException // 비밀번호 암호화 알고리즘 존재 확인
     */
    public void validatePassword(UserDTO user, String loginPassword) throws CredentialException, NoSuchAlgorithmException {
        String hashedPassword = userService.plainToSha256(loginPassword);
        if (!user.getPassword().equals(hashedPassword)) {
            int failCount = userService.addLoginFailCount(user); // 로그인 실패 횟수 +1
            if(failCount>=5){ // 로그인 실패횟수가 5회 이상
                userService.updateLoginLock(user); // loginLock 시간 update
            }
            throw new CredentialException("비밀번호가 일치하지 않습니다.");
        }
        // 로그인 성공, 로그인 실패 횟수 및 로그인 락 해제
        userService.resetLoginFailCountAndLoginLocked(user);
    }

    /**
     * 로그인 후, 액세스 토큰과 리프레시 토큰을 발급함.
     * @param user // 액세스 토큰 발급에 필요한 유저 정보
     * @return issuedTokens // accessJWT, refreshJWT
     */
    public TokensDTO issueTokens(UserDTO user) {
        TokensDTO issuedTokens = new TokensDTO();
        issuedTokens.setAccessToken(generateAccessJWT(user));
        issuedTokens.setRefreshToken(generateRefreshJWT());

        // redis 에 저장
        redisService.setValues(issuedTokens.getAccessToken(), issuedTokens.getRefreshToken());

        return issuedTokens;
    }

    /**
     * 유저의 마지막 비밀번호 업데이트 기간 확인, 3개월 이상 시 예외 처리
     * @param user
     * @param issueTokens // 비밀번호 업데이트 하라는 메시지와 함께 access token 전달 필요
     * @throws LastPwException // 3개월 이상 비번 변경 안 했을 시 예외 처리
     */
    public void validateLastPwUpdate(UserDTO user, TokensDTO issueTokens) throws LastPwException {
        //마지막 비번 변경 확인
        Date currentDate = new Date();
        long lastPwUpdatedFromToday = (currentDate.getTime() - user.getLastPwUpdated().getTime()) / (1000 * 60 * 60 * 24);
        // 3개월 이상 비번 변경 x
        if(lastPwUpdatedFromToday > 3*month ) throw new LastPwException(String.valueOf(issueTokens));
    }

    /**
     * 유저 계정이 잠금 상태인지 확인, 잠금 상태라면 예외 처리
     * @param user
     * @throws UserIsLockedException // 관리자에게 문의하라는 예외 처리
     */
    public void validateUserLocked(UserDTO user) throws UserIsLockedException {
        if(user.getIsLocked()==lockUser) throw new UserIsLockedException();
    }

    /**
     * (로그인 실패가 5회 이상) && (마지막 시도로부터 5분 미만)이면 로그인할 수 없게 예외 처리
     * @param user
     * @throws LoginLockException
     */
    public void validateLoginAttempts(UserDTO user) throws LoginLockException {
        Date currentDate = new Date();
        //login 제한이 걸려있는지, 5번 이상 로그인 실패 했는지 확인
        if(user.getLoginLocked() != null && user.getLoginFail() >= 5){
            // login 제한이 걸린 시각으로부터 현재 시각까지의 차이를 구함
            long lastLoginLockFromNow = (currentDate.getTime() - user.getLoginLocked().getTime()) / (1000 * 60);
            // 로그인 제한은 5분이므로 5분보다 짧으면 Login을 할 수 없게 LoginLockException을 던짐
            if(lastLoginLockFromNow<5) throw new LoginLockException();
            //만약 5분이 지났다면 로그인 시도 제한은 풀림
        }
    }

    /**
     * 마지막 로그인으로부터 1개월 이상 접속 하지 않은 경우, 계정을 잠금 (휴면 계정), 관리자에게 문의해서 잠금을 해제해야 함
     * @param user
     * @throws LastLoginException
     */
    public void validateLastLogin(UserDTO user) throws LastLoginException {
        Date currentDate = new Date();
        // 마지막 로그인으로부터 현재 로그인한 날짜 차이를 계산
        long lastLoginFromToday = (currentDate.getTime() - user.getLastLogin().getTime()) / (1000 * 60 * 60 * 24);
        if(lastLoginFromToday > month) {  // 1개월 이상 접속(로그인) 하지 않은 경우, 계정을 잠그고, 로그인을 못 하게 함.
            userService.lockUnlockUser(user.getId(), lockUser);
            throw new LastLoginException();
        }
    }

//    /**
//     * access 토큰의 유효성 확인
//     * @param accessJWT
//     */
//    public void checkAccessJWTValid(String accessJWT){
//        if(accessJWT == null || !accessJWT.startsWith("Bearer ") ) { // 7자 이상 조건도 만족
//            throw new MalformedJwtException("유효하지 않은 토큰 형식입니다.");
//        }
//        String jwtToken = accessJWT.substring(7);
//        try {
//            extractJWTClaims(jwtToken);
//        } catch (ExpiredJwtException e) {
//            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "access token이 만료되었습니다. 새로운 액세스 토큰을 발급받으세요.");
//        }
//    }

    /**
     * 액세스 JWT와 리프레시 JWT의 유효성을 검증하고, 필요에 따라 액세스 JWT를 재발급합니다.
     * @param accessJWT 검증할 액세스 JWT
     * @param refreshJWT 검증할 리프레시 JWT
     * @return 새로운 액세스 JWT를 반환합니다.
     * @throws RuntimeException JWT 검증 실패 시 예외를 던집니다.
     */
    public String checkRefreshJWTValid(String accessJWT, String refreshJWT) throws RuntimeException, TokenNotIssueException {

        validateAccessTokenFormat(accessJWT);
        validateRefreshToken(accessJWT, refreshJWT);
        return attemptReissueAccessToken(accessJWT, refreshJWT);
    }

    public String validateAccessTokenFormat(String accessJWT) throws MalformedJwtException {
        if(StringUtils.isBlank(accessJWT) || !accessJWT.startsWith("Bearer ")) { // 7자 이상 조건도 만족
            throw new MalformedJwtException("유효하지 않은 토큰 형식입니다.");
        }
        return accessJWT.substring(7);
    }

    public void validateAccessTokenClaim(String accessJWT) {
        try {
            extractJWTClaims(accessJWT);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "access token이 만료되었습니다. 새로운 액세스 토큰을 발급받으세요.");
        }
    }

    public void validateRefreshToken(String accessJWT, String refreshJWT) {
        // redis에 저장된 refresh token 가져오기
        String storedRefreshJWT = redisService.getValues(accessJWT);

        // redis와 입력받은 refresh token이 일치하지 않는 경우
        if(!refreshJWT.equals(storedRefreshJWT)) throw new NotFoundException("access token에 해당하는 refresh token이 존재하지 않습니다. 다시 로그인 해주세요.");

        // refresh token도 만료되지 않았는지 확인
        validateRefreshTokenClaim(refreshJWT);
    }

    public void validateRefreshTokenClaim(String refreshJWT) {
        try {
            extractJWTClaims(refreshJWT);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "refresh token이 만료되었습니다. 다시 로그인해주세요.");
        }
    }

    public String attemptReissueAccessToken(String accessJWT, String refreshJWT) throws TokenNotIssueException {
        try {
            extractJWTClaims(accessJWT);
        } catch (ExpiredJwtException e) {
            return reissueAccessToken(e, refreshJWT);
        }
        throw new TokenNotIssueException("액세스 토큰이 만료되지 않아 새 액세스 토큰을 발급할 수 없습니다.");
    }

    public String reissueAccessToken(ExpiredJwtException e, String refreshJWT) {
        // access token은 만료되어야 새로 access token을 발급 해줌.(무한 발급 방지)
        int id = Integer.parseInt(e.getClaims().getSubject());
        String userId = e.getClaims().get("userId", String.class);
        //FIXME: getId가 accesstoken이랑 동일한지 확인 필요
        redisService.deleteValues(e.getClaims().getId()); // 기존 accessJWT(key), refresh(value) pair는 redis에서 삭제

        String newAccessJWT = generateAccessJWT(id, userId); // 새로운 Access JWT 발급
        redisService.setValues(newAccessJWT, refreshJWT); // redis에 새 조합 등록
        return newAccessJWT;
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

}
