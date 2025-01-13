package com.example.recommendation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthInfoDTO {
    private long userId;
    private long age;
    private Double weight;
    private Double height;
    private Double bmi;
    private Double bmr;
    private String gender; // MALE, FEMALE
    private Double sugarLevel;
    private boolean sinusitis;
    private boolean previousStroke;
    private String thirstLevel; // NORMAL, MORE_THAN_USUAL
    private String frequencyOfUrination; // NORMAL, MORE_THAN_USUAL
    private String blurredVision; // RARELY, OFTEN
    private String alcoholConsumption; //  NONE, OCCASIONAL, MODERATE, HEAVY
    private String bloodGroup;  //  A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE
    private String goalType;  // LOSE_WEIGHT, BUILD_MUSCLE, IMPROVE_FITNESS, REDUCE_STRESS, IMPROVE_SLEEP
    private String activityLevel;  // SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE
}
