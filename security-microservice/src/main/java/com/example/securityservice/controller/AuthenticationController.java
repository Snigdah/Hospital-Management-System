package com.example.securityservice.controller;

import com.example.securityservice.DTO.request.LogInRequestDTO;
import com.example.securityservice.DTO.request.RegisterRequestDTO;
import com.example.securityservice.DTO.response.AuthenticationResponseDTO;
import com.example.securityservice.DTO.response.LogInResponseDTO;
import com.example.securityservice.response.ResponseHandler;
import com.example.securityservice.service.IAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LogInRequestDTO request){
        LogInResponseDTO response = authenticationService.authenticate(request);
        return ResponseHandler.generateResponse("Account Login Successfully", HttpStatus.OK, response);
    }
}
