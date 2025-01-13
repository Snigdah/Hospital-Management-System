package com.example.recommendation.controller;

import com.example.recommendation.dto.response.ConsultationResponseDTO;
import com.example.recommendation.service.IMedicalConsultationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/recommendation/consult")
public class ConsultationController {
    private final IMedicalConsultationService consultationService;
    public ConsultationController(IMedicalConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("/start")
    public ResponseEntity<ConsultationResponseDTO> startConsultation() {
        consultationService.resetConversation();
        return ResponseEntity.ok(consultationService.getNextStep(null));
    }

    @PostMapping("/respond")
    public ResponseEntity<ConsultationResponseDTO> respondToQuestion(@RequestBody Map<String, String> response) {
        String userResponse = response.getOrDefault("answer", "");
        ConsultationResponseDTO nextStep = consultationService.getNextStep(userResponse);
        return ResponseEntity.ok(nextStep);
    }
}
