package com.example.weboard.exception;

public class GenerateNewAccessJWTException extends Exception{
    public GenerateNewAccessJWTException(){}

    public GenerateNewAccessJWTException(String message){
        super(message);
    }

    public GenerateNewAccessJWTException(Throwable throwable){
        super(throwable);
    }

    public GenerateNewAccessJWTException(String message, Throwable throwable){
        super(message, throwable);
    }
}
