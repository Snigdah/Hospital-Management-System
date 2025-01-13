package com.example.patient.controller;

import com.example.patient.dto.request.HealthProfileDTO;
import com.example.patient.response.ResponseHandler;
import com.example.patient.services.IAuthenticationService;
import com.example.patient.services.IHealthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/health-info")
public class HealthController {
    private final IHealthService healthService;
    private final IAuthenticationService authenticationService;

    public HealthController(IHealthService healthService, IAuthenticationService authenticationService) {
        this.healthService = healthService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createHealthProfile(@Valid @RequestBody HealthProfileDTO healthProfileDTO){
        long userId = authenticationService.getAuthenticatedUser();
        healthService.createHealthProfile(userId, healthProfileDTO);
        return ResponseHandler.generateResponse("Create Health Profile Successfully", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateHealthProfile(@Valid @RequestBody HealthProfileDTO healthProfileDTO){
        long userId = authenticationService.getAuthenticatedUser();
        healthService.updateHealthProfile(userId, healthProfileDTO);
        return ResponseHandler.generateResponse("Update Health Profile Successfully", HttpStatus.CREATED);
    }
}
