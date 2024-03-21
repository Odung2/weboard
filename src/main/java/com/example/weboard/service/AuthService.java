package com.example.weboard.service;

import org.springframework.stereotype.Service;
import com.example.weboard.mapper.CommentMapper;
import com.example.weboard.dto.CommentDTO;
import com.example.weboard.mapper.UserMapper;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.mapper.PostMapper;
import com.example.weboard.dto.PostDTO;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final UserService userService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public AuthService(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    public String authenticateAndGetToken(String userId, String password) throws NoSuchAlgorithmException {
        // 유저 아이디로 유저 정보 가져오기
        Integer id = userService.getIdByUserId(userId);
        if (id == null) {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }

        // 유저 비밀번호 확인
        String storedPassword = userService.getPasswordById(id);
        String hashedPassword = userService.plainToSha256(password);
        if (!storedPassword.equals(hashedPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        UserDTO user = userService.getUserById(id);
        // JWT 토큰 생성
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .claim("userId", user.getUserId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
