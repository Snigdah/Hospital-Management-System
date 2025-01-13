package com.example.recommendation.service;

import com.example.recommendation.dto.response.ConsultationResponseDTO;

public interface IMedicalConsultationService {
    ConsultationResponseDTO getNextStep(String userResponse);
    void resetConversation();
}
