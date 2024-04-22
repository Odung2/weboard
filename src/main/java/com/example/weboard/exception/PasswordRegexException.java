package com.example.weboard.exception;


public class PasswordRegexException extends Exception{

    public PasswordRegexException(){}

    public PasswordRegexException(String message){
        super(message);
    }

    public PasswordRegexException(Throwable throwable){
        super(throwable);
    }

    public PasswordRegexException(String message, Throwable throwable){
        super(message, throwable);
    }
}
