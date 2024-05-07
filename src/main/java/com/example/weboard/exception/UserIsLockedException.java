package com.example.weboard.exception;

public class UserIsLockedException extends RuntimeException{

    public UserIsLockedException(){}

    public UserIsLockedException(String message){
        super(message);
    }

    public UserIsLockedException(Throwable throwable){
        super(throwable);
    }

    public UserIsLockedException(String message, Throwable throwable){
        super(message, throwable);
    }
}
