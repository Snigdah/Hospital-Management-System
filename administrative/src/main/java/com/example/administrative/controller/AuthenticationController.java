package com.example.administrative.controller;

import com.example.administrative.dto.request.RegisterDTO;
import com.example.administrative.dto.request.RegisterRequestDTO;
import com.example.administrative.dto.response.AuthenticationResponseDTO;
import com.example.administrative.response.ResponseHandler;
import com.example.administrative.services.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/administrative/auth")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDTO request){
        AuthenticationResponseDTO responseDTO = authenticationService.register(request);
        return ResponseHandler.generateResponse("Create Account Successfully", HttpStatus.CREATED, responseDTO);
    }

}
