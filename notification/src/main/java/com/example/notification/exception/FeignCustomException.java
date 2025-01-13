package com.example.notification.exception;

import com.example.notification.response.ErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class FeignCustomException extends RuntimeException{
    private final HttpStatus statusCode;
    private final ErrorDetails errorDetails;

    public FeignCustomException(HttpStatus statusCode, Response response, ObjectMapper objectMapper) {
        super("Feign error: " + statusCode + ", Response: " + response);

        this.statusCode = statusCode;
        this.errorDetails = deserializeErrorDetails(response, objectMapper);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    private ErrorDetails deserializeErrorDetails(Response response, ObjectMapper objectMapper) {
        String responseBody = "";
        try {
            responseBody = response.body() != null ? Util.toString(response.body().asReader()) : "";
            return objectMapper.readValue(responseBody, ErrorDetails.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize error details: " + responseBody, e);
        }
    }
}


