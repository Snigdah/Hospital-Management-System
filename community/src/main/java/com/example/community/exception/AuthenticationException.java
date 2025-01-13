package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public AuthenticationException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public AuthenticationException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message =  message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

