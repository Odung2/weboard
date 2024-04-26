package com.example.weboard.interceptors;

import com.example.weboard.exception.GenerateNewAccessJWTException;
import com.example.weboard.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-expiration}")
    private int jwtExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private int refreshJWTExpiration;

    private final AuthService authService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURL = request.getRequestURL().toString();
        // FIXME : request uri를 사용해야한다.
        // FIXME : api uri를 따로 만드는것이 더 좋음
        if (request.getMethod().equals("GET") && requestURL.startsWith("http://localhost:8080/weboard/comments/") ){
            return true;
        }
        if (request.getMethod().equals("GET") && requestURL.startsWith("http://localhost:8080/weboard/posts/") ){
            return true;
        }

        String accessJWT = request.getHeader("Authorization");
        String refreshJWT = request.getHeader("Refresh-token");

        if(StringUtils.isBlank(refreshJWT)){
            authService.checkAccessJWTValid(accessJWT);
        }else{
            String newAccessToken=authService.checkJWTValid(accessJWT, refreshJWT);
            if (newAccessToken != null && !newAccessToken.isEmpty()) {
                throw new GenerateNewAccessJWTException(newAccessToken); // 새로 access token 발급 됨
            }
        }
        int userId = authService.getIdFromToken(accessJWT);
        request.setAttribute("reqUserId", userId);
        //RequestContextHolder.


        return true;
    }
}
