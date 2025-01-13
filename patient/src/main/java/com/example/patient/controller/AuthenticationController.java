package com.example.patient.controller;

import com.example.patient.dto.request.PatientRegisterDTO;
import com.example.patient.dto.response.AuthenticationResponseDTO;
import com.example.patient.response.ResponseHandler;
import com.example.patient.services.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient/auth")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody PatientRegisterDTO request){
        AuthenticationResponseDTO responseDTO = authenticationService.registerPatient(request);
        return ResponseHandler.generateResponse("Create Account Successfully", HttpStatus.CREATED, responseDTO);
    }
}
