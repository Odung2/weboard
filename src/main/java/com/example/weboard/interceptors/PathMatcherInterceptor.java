package com.example.weboard.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.RequestPath;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@RequiredArgsConstructor
public class PathMatcherInterceptor implements HandlerInterceptor {

    private final HandlerInterceptor handlerInterceptor;
    private final PathContainer pathContainer;

    public PathMatcherInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
        this.pathContainer = new PathContainer();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // pathContainer에 해당 요청 url과 메서드가 포함되지 않다면 바로 서비스 로직으로 요청이 간다.
        if (pathContainer.notIncludedPath(request.getServletPath(), request.getMethod())) {
            return true;
        }

        // 해당 요청 url과 메서드가 포함이 되어있다면 본 인터셉터(로그인 인터셉터)에 요청을 위임한다. (인터셉터 기능 실행)
        return handlerInterceptor.preHandle(request, response, handler);
    }

    // 외부에서 적용 path 패턴을 추가할 때
    public PathMatcherInterceptor includePathPattern(String pathPattern, PathMethod pathMethod) {
        pathContainer.includePathPattern(pathPattern, pathMethod);
        return this;
    }

    // 외부에서 미적용 path 패턴을 추가할 때
    public PathMatcherInterceptor excludePathPattern(String pathPattern, PathMethod pathMethod) {
        pathContainer.excludePathPattern(pathPattern, pathMethod);
        return this;
    }
}