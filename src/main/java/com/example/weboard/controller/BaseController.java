package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.weboard.dto.FrkConstants;

public class BaseController {

    private <T> ResponseEntity<ApiResponse<T>> responseEntity(int resultCode, String resultMessage, T data) {
        ApiResponse<T> resp = new ApiResponse<T>(resultCode, resultMessage, data);
        return new ResponseEntity<ApiResponse<T>>(resp, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ApiResponse<T>> ok(T body) {

        return responseEntity(FrkConstants.CD_OK, FrkConstants.SUCCESS, body);

    }

    protected <T> ResponseEntity<ApiResponse<T>> nok(int resultCode, String message, T body) {
        return responseEntity(resultCode, message, body);
    }

    protected <T> ResponseEntity<ApiResponse<T>> nok(int resultCode, T body) {
        return nok(resultCode, FrkConstants.FAIL, body);
    }

    protected ResponseEntity<ApiResponse<Void>> ok() {

        return responseEntity(FrkConstants.CD_OK, FrkConstants.SUCCESS, null);

    }

    protected <T> ResponseEntity<ApiResponse<T>> nok(T body) {
        return responseEntity(FrkConstants.CD_NOK, FrkConstants.FAIL, body);

    }

    protected <T> ResponseEntity<ApiResponse<T>> nok(int resultCode) {
        return nok(resultCode, null);
    }

    protected ResponseEntity<ApiResponse<Void>> nok() {
        return nok(FrkConstants.CD_NOK);
    }
}
