package com.example.administrative.exception;

import org.springframework.http.HttpStatus;

public class CustomeException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public CustomeException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public CustomeException(String message, HttpStatus status, String message1) {
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
