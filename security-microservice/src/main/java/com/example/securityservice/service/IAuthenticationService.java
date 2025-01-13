package com.example.securityservice.service;

import com.example.securityservice.DTO.response.AuthenticationResponseDTO;
import com.example.securityservice.DTO.request.LogInRequestDTO;
import com.example.securityservice.DTO.response.LogInResponseDTO;
import com.example.securityservice.DTO.request.RegisterRequestDTO;

public interface IAuthenticationService {
    public AuthenticationResponseDTO register(RegisterRequestDTO request);
    LogInResponseDTO authenticate(LogInRequestDTO request);
}
