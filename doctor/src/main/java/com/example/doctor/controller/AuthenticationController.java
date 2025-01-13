package com.example.doctor.controller;

import com.example.doctor.dto.request.DoctorRegistrationDTO;
import com.example.doctor.dto.response.AuthenticationResponseDTO;
import com.example.doctor.dto.response.RegistrationResponseDTO;
import com.example.doctor.response.ResponseHandler;
import com.example.doctor.services.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor/auth")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody DoctorRegistrationDTO request){
        RegistrationResponseDTO responseDTO = authenticationService.registerDoctor(request);
        return ResponseHandler.generateResponse("Create Account Successfully", HttpStatus.CREATED, responseDTO);
    }
}
