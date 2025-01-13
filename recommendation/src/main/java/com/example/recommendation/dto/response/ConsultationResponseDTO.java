package com.example.recommendation.dto.response;
import lombok.Data;

@Data
public class ConsultationResponseDTO {
    private String message;
    private boolean isConversationEnded;

    // Constructor, getters, and setters

    public ConsultationResponseDTO(String message, boolean isConversationEnded) {
        this.message = message;
        this.isConversationEnded = isConversationEnded;
    }
}