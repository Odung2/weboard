package com.example.weboard.exception;

import com.example.weboard.controller.BaseController;
import com.example.weboard.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.coyote.BadRequestException;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtSignatureException(SignatureException e){
//        ApiResponse apiResponse= new ApiResponse(400, "토큰의 서명이 올바르지 않습니다.", null);
//        return ResponseEntity.status(400).body(apiResponse);
        return nok(400, "토큰의 서명이 올바르지 않습니다.",null);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleExpiredJwtException(ExpiredJwtException e){
//        ApiResponse apiResponse = new ApiResponse(403, "토큰이 만료되었습니다", null);
//        return ResponseEntity.status(403).body(apiResponse);
        return nok(403, e.getMessage(), null);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleMalformedJwtException (MalformedJwtException e){
//        ApiResponse apiResponse = new ApiResponse(400, "토큰의 형식이 올바르지 않습니다", null);
//        return ResponseEntity.status(400).body(apiResponse);
        return nok(400, e.getMessage(), null);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(JwtException e){
//        ApiResponse apiResponse = new ApiResponse(500, "토큰 처리 중 문제가 생겼습니다", null);
//        return ResponseEntity.status(500).body(apiResponse);
        return nok(400, "토큰 처리 중 문제가 생겼습니다.", null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException e){
        return nok(400, e.getMessage(), null);
    }

    @ExceptionHandler(ShortPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleShortPasswordException(ShortPasswordException e){
        return nok(403, "비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자의 조합으로 입력해주세요.");
    }

    @ExceptionHandler(PasswordRegexException.class)
    public ResponseEntity<ApiResponse<Object>> handlePasswordRegexException(PasswordRegexException e){
        return nok(403, e.getMessage(), null);
    }

    @ExceptionHandler(LoginLockException.class)
    public ResponseEntity<ApiResponse<Object>> handleLoginLockException(LoginLockException e){
        return nok(403, "5회 이상 로그인 실패 시 로그인 시도가 5분간 제한됩니다.", null);
    }

    @ExceptionHandler(UserIsLockedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserIsLockedException(UserIsLockedException e){
        return nok(403, "계정이 잠겨있습니다. 관리자에게 문의하세요.", null);
    }

    @ExceptionHandler(LastPwException.class)
    public ResponseEntity<ApiResponse<Object>> handleLastPwException(LastPwException e){
        return nok(403, "비밀번호를 변경한지 3개월이 지났습니다. 보안을 위해 비밀번호를 변경해주세요.", null);
    }

    @ExceptionHandler(LastLoginException.class)
    public ResponseEntity<ApiResponse<Object>> handleLastLoginException(LastLoginException e){
        return nok(403, "마지막 로그인이 1개월 이전이라 계정을 임시잠금조치하였습니다. 관리자에게 문의하세요.", null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e){
        return nok(500, e.getMessage(), e.getClass());
    }

    @ExceptionHandler(GenerateNewAccessJWTException.class)
    public ResponseEntity<ApiResponse<Object>> handleGenerateNewAccessJWTException(GenerateNewAccessJWTException e){
        //e.getMessage()는 access token
        return nok(500,"발급한 새 Access 토큰으로 접속해주세요." , e.getMessage());
    }

}
