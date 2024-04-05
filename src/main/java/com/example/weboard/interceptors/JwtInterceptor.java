package com.example.weboard.interceptors;

import com.example.weboard.service.AuthService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HttpServletBean;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JwtInterceptor implements HandlerInterceptor {


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("GET")){
            String requestURL = request.getRequestURL().toString();
            if(requestURL.startsWith("http://localhost:8080/weboard/comments/")){
                return true;
            }
        }
        String jwtToken = request.getHeader("Authorization");

        if(jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized
            return false;
        }

        String token = jwtToken.substring(7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwtToken);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new MalformedJwtException("토큰이 유효하지 않습니다.");
        }

        return true;
    }
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
