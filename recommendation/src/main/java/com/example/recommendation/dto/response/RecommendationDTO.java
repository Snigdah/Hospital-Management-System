package com.example.recommendation.dto.response;

import com.example.recommendation.entity.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDTO {
    private long id;
    private long userId;
    private Double dailyCalorieIntake;
    private Double proteinIntake;
    private Double recommendedWeight;
    private Double fatIntake;
    private Double preferredBMR;
    private Double preferredBMI;
    private long diabetesRisk;
    private RiskLevel riskLevel;
}
