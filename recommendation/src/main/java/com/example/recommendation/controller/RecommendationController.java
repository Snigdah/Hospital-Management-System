package com.example.recommendation.controller;

import com.example.recommendation.dto.response.ProgressDTO;
import com.example.recommendation.dto.response.RecommendationDTO;
import com.example.recommendation.response.ResponseHandler;
import com.example.recommendation.service.IAuthenticationService;
import com.example.recommendation.service.IProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendation")
public class RecommendationController {
    private final IProgressService progressService;
    private final IAuthenticationService authenticationService;

    public RecommendationController(IProgressService progressService, IAuthenticationService authenticationService) {
        this.progressService = progressService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/progress")
    public ResponseEntity<Object> getProgress(){
        long userId = authenticationService.getAuthenticatedUser();
        List<ProgressDTO>  response = progressService.getLastFiveProgressRecords(userId);
        return ResponseHandler.generateResponse("Fetch Progress successfully", HttpStatus.OK, response);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getRecommendations(){
        long userId = authenticationService.getAuthenticatedUser();
        RecommendationDTO response = progressService.getRecommendationByUserId(userId);
        return ResponseHandler.generateResponse("Fetch data Successfully", HttpStatus.OK, response);
    }

}
