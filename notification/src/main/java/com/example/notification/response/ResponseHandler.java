package com.example.notification.response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static com.example.notification.utils.Constants.*;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> response = new HashMap<String, Object>();
        Map<String, Object> meta = new HashMap<String, Object>();

        meta.put(STATUS_KEY, status);
        meta.put("code", status.value());
        meta.put(MESSAGE_KEY, message);

        response.put("meta", meta);

        if(responseObj != null){
            response.put(DATA_KEY, responseObj);
        }

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    // Overloaded method for cases where data is not needed Like Delete etc.
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
        return generateResponse(message, status, null);
    }
}
