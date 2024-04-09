package com.example.weboard.interceptors;

import com.example.weboard.service.AuthService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
        String requestURL = request.getRequestURL().toString();
        if (request.getMethod().equals("GET") && requestURL.startsWith("http://localhost:8080/weboard/comments/") ){
            return true;
        }
        String jwtToken = request.getHeader("Authorization");

        if(jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized
            return false;
        }

        String token = jwtToken.substring(7);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken);
        int idFromJwt = Integer.parseInt(claims.getBody().getSubject());
        if(requestURL.startsWith("http://localhost:8080/weboard/users/")){
            String[] uriParts = requestURL.split("/");
            int id = Integer.parseInt(uriParts[uriParts.length-1]);

            if(idFromJwt!=id){ //로그인 유저와 요청 정보의 유저가 다른 사람일 경우
                switch (request.getMethod()){
                    case "GET":
                        throw new BadRequestException("BAD_REQUEST: 본인이 아닌 유저의 정보를 확인할 수 없습니다.");
                    case "PUT":
                        throw new BadRequestException("BAD_REQUEST: 본인이 아닌 유저의 정보를 수정할 수 없습니다.");
                    case "DELETE":
                        throw new BadRequestException("BAD_REQUEST: 본인이 아닌 유저의 정보를 삭제할 수 없습니다.");
                    default:
                        break;
                }
            }

        }

        return true;
    }
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
