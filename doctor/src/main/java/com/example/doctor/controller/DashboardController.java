package com.example.doctor.controller;

import com.example.doctor.dto.response.DashboardResponseDTO;
import com.example.doctor.response.ResponseHandler;
import com.example.doctor.services.IAuthenticationService;
import com.example.doctor.services.IDashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor/dashboard")
public class DashboardController {

    private final IDashboardService dashboardService;
    private final IAuthenticationService authenticationService;

    public DashboardController(IDashboardService dashboardService, IAuthenticationService authenticationService) {
        this.dashboardService = dashboardService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/count")
    public ResponseEntity<Object> getCount(){
        long userId = authenticationService.getAuthenticatedUser();
        DashboardResponseDTO response = dashboardService.getDashboardCount(userId);
        return ResponseHandler.generateResponse("Fetch Data Successfully", HttpStatus.OK, response);
    }
}
