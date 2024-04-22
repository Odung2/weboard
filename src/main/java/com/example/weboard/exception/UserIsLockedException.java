package com.example.weboard.exception;

public class UserIsLockedException extends Exception{

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
