package com.example.recommendation.service.impl;

import com.example.recommendation.exception.AuthenticationException;
import com.example.recommendation.service.IAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {
    // Get userId from Principal
    public long getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return Long.parseLong(authentication.getName());
        } else {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
    }
}
