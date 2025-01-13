package com.example.notification.utils;

import com.example.notification.exception.CustomeException;
import com.example.notification.model.enums.Role;
import org.springframework.http.HttpStatus;

public class EnumValidation {
    public static Role parseRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid Role. Supported Roles are PATIENT, DOCTOR, ADMINISTRATIVE ");
        }
    }
}
