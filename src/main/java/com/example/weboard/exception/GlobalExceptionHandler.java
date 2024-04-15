package com.example.weboard.exception;

import com.example.weboard.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtSignatureException(SignatureException e){
        ApiResponse apiResponse= new ApiResponse(400, "토큰의 서명이 올바르지 않습니다.", null);
        return ResponseEntity.status(400).body(apiResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleExpiredJwtException(ExpiredJwtException e){
        ApiResponse apiResponse = new ApiResponse(403, "토큰이 만료되었습니다", null);
        return ResponseEntity.status(403).body(apiResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleMalformedJwtException (MalformedJwtException e){
        ApiResponse apiResponse = new ApiResponse(400, "토큰의 형식이 올바르지 않습니다", null);
        return ResponseEntity.status(400).body(apiResponse);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(JwtException e){
        ApiResponse apiResponse = new ApiResponse(500, "토큰 처리 중 문제가 생겼습니다", null);
        return ResponseEntity.status(500).body(apiResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException e){
        ApiResponse apiResponse = new ApiResponse(400, e.getMessage(), null);
        return ResponseEntity.status(400).body(apiResponse);
    }
}
