package com.example.administrative.controller;

import com.example.administrative.dto.response.DashboardCountDTO;
import com.example.administrative.response.ResponseHandler;
import com.example.administrative.services.IDashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/administrative/dashboard")
public class DashBoardController {
    private final IDashboardService dashboardService;
    public DashBoardController(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/get-count")
    public ResponseEntity<Object> getDashboardCount(){
        DashboardCountDTO response = dashboardService.getDashboardCount();
        return ResponseHandler.generateResponse("Fetch Data Successfully", HttpStatus.OK, response);
    }
}
