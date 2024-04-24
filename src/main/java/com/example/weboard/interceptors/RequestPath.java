package com.example.weboard.interceptors;

import lombok.Getter;

public class RequestPath {
    @Getter
    private String pathPattern;
    private PathMethod method;
    public RequestPath(String pathPattern, PathMethod method) {
        this.pathPattern = pathPattern;
        this.method = method;
    }

    public boolean matchesMethod(String method) {
        return this.method.name().equalsIgnoreCase(method);
    }
}

