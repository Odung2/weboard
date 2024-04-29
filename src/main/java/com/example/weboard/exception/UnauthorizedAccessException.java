package com.example.weboard.exception;

public class UnauthorizedAccessException extends Exception{

    public UnauthorizedAccessException(){}

    public UnauthorizedAccessException(String message){
        super(message);
    }

    public UnauthorizedAccessException(Throwable throwable){
        super(throwable);
    }

    public UnauthorizedAccessException(String message, Throwable throwable){
        super(message, throwable);
    }
}
