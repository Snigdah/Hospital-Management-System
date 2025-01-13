package com.example.administrative.services.impl;

import com.example.administrative.dto.request.RegisterDTO;
import com.example.administrative.dto.request.RegisterRequestDTO;
import com.example.administrative.dto.response.AuthenticationResponseDTO;
import com.example.administrative.exception.AuthenticationException;
import com.example.administrative.exception.FeignCustomException;
import com.example.administrative.feignclient.SecurityServiceClient;
import com.example.administrative.services.IAuthenticationService;
import com.example.administrative.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService implements IAuthenticationService {
    private final SecurityServiceClient securityServiceClient;

    public AuthenticationService(SecurityServiceClient securityServiceClient) {
        this.securityServiceClient = securityServiceClient;
    }

    // For Register
    @Override
    public AuthenticationResponseDTO register(RegisterDTO request) {

        //Convert Dto to model
        RegisterRequestDTO requestData = RegisterRequestDTO
                .builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Constants.ROLE_ADMINISTRATIVE) // Set Role as Administrator
                .build();

        AuthenticationResponseDTO register;
        try {
            log.info("Calling feing client for registration");
            register = securityServiceClient.register(requestData);
        } catch (FeignCustomException ex) {
            log.error("An error occurred during account creation", ex);
            throw new AuthenticationException(ex.getStatusCode(), ex.getErrorDetails().getMessage());
        }
        return register;
    }
}
