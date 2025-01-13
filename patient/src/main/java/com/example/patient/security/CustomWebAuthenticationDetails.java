package com.example.patient.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private final String jwtToken;

    public CustomWebAuthenticationDetails(HttpServletRequest request, String jwtToken) {
        super(request);
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
