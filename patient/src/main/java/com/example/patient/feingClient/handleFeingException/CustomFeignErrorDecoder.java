package com.example.patient.feingClient.handleFeingException;

import com.example.patient.exception.FeignCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class CustomFeignErrorDecoder {
    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, Response response) {
                HttpStatus statusCode = HttpStatus.valueOf(response.status());
                // Create a custom exception with the status code and response body
                return new FeignCustomException(statusCode, response, objectMapper);
            }
        };
    }
}