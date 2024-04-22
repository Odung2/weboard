package com.example.weboard.exception;

public class LastLoginException extends Exception{
    public LastLoginException(){}

    public LastLoginException(String message){
        super(message);
    }

    public LastLoginException(Throwable throwable){
        super(throwable);
    }

    public LastLoginException(String message, Throwable throwable){
        super(message, throwable);
    }
}
