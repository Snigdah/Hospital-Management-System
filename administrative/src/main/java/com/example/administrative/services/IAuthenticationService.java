package com.example.administrative.services;

import com.example.administrative.dto.request.RegisterDTO;
import com.example.administrative.dto.request.RegisterRequestDTO;
import com.example.administrative.dto.response.AuthenticationResponseDTO;

public interface IAuthenticationService {
    public AuthenticationResponseDTO register(RegisterDTO request);
}