package com.example.weboard.service;

import com.example.weboard.dto.ApiResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.weboard.mapper.CommentMapper;
import com.example.weboard.dto.CommentDTO;
import com.example.weboard.mapper.UserMapper;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.mapper.PostMapper;
import com.example.weboard.dto.PostDTO;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final UserService userService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public Boolean compareJwtToId(int id, String jwttoken){
        int idFromJwt = getIdFromToken(jwttoken);
        if (idFromJwt!=id){
            return false;
        }
        return true;
    }
    public ResponseEntity<ApiResponse> loginAndJwtProvide(String userId, String password) { //순서에 영향을 받음 PARAM을 .. 재사용성이 있으면 parameter을 써라
        UserDTO user = userService.getUserByIdOrUserId(userId).getBody().getData();
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        String storedPassword = user.getPassword();
        String hashedPassword = userService.plainToSha256(password);
        if (!storedPassword.equals(hashedPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        String token = generateJwtToken(user);
        ApiResponse apiResponse = new ApiResponse(0, "성공적으로 로그인하였습니다.", token);
        return ResponseEntity.status(200).body(apiResponse);
    }

    private String generateJwtToken(UserDTO user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .claim("userId", user.getUserId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

    }

    public Integer getIdFromToken(String jwttoken) {
        if (jwttoken == null || !jwttoken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없거나 유효하지 않습니다.");
        }
        String jwtToken = jwttoken.substring(7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwtToken);

            return Integer.parseInt((String) claims.getBody().getSubject());
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


}
