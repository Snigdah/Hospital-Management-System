package com.example.recommendation.service.impl;

import com.example.recommendation.dto.request.HealthInfoDTO;
import com.example.recommendation.entity.Recommendation;
import com.example.recommendation.entity.enums.ActivityLevel;
import com.example.recommendation.entity.enums.Gender;
import com.example.recommendation.entity.enums.GoalType;
import com.example.recommendation.entity.enums.RiskLevel;
import com.example.recommendation.repository.RecommendationRepository;
import com.example.recommendation.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    // Check if previous data is available or not
    public Optional<Recommendation> getPreviousRecommendation(HealthInfoDTO healthInfoDTO) {
        return recommendationRepository.findByUserId(healthInfoDTO.getUserId());
    }

    // Create or Update Recommendation
    public void createRecommendation(HealthInfoDTO healthInfoDTO) {
        Optional<Recommendation> previousRecommendation = getPreviousRecommendation(healthInfoDTO);

        if (previousRecommendation.isPresent()) {
            // Update previous recommendation
            buildRecommendation(healthInfoDTO, previousRecommendation.get());
        } else {
            // Create new recommendation
            Recommendation recommendation = new Recommendation();
            buildRecommendation(healthInfoDTO, recommendation);
        }
    }

    // Process data based on recommendation
    private void buildRecommendation(HealthInfoDTO healthInfoDTO, Recommendation recommendation) {
        recommendation.setUserId(healthInfoDTO.getUserId());
        recommendation.setDailyCalorieIntake(calculateDailyCalorieIntake(healthInfoDTO));
        recommendation.setProteinIntake(calculateProteinIntake(healthInfoDTO));
        recommendation.setFatIntake(calculateFatIntake(healthInfoDTO));
        recommendation.setRecommendedWeight(calculateRecommendedWeight(healthInfoDTO));
        recommendation.setPreferredBMR(calculatePreferredBMR(healthInfoDTO));
        recommendation.setPreferredBMI(calculatePreferredBMI(healthInfoDTO));
        long riskScore = calculateDiabetesRiskScore(healthInfoDTO);
        recommendation.setDiabetesRisk(riskScore);
        recommendation.setRiskLevel(determineRiskLevel(riskScore));

        // Save in Database
        recommendationRepository.save(recommendation);
    }


    // Calculate daily Calorie
    private Double calculateDailyCalorieIntake(HealthInfoDTO healthInfoDTO) {
        // Get user-specific data
        GoalType goalType = GoalType.valueOf(healthInfoDTO.getGoalType());
        ActivityLevel activityLevel = ActivityLevel.valueOf(healthInfoDTO.getActivityLevel());
        double bmr = healthInfoDTO.getBmr(); // Get BMR from HealthDetails

        switch (goalType) {
            case LOSE_WEIGHT -> bmr *= 0.85; // Reduce calorie intake for weight loss
            case BUILD_MUSCLE -> bmr *= 1.15; // Increase calorie intake for muscle building
            case IMPROVE_FITNESS -> bmr *= 1.1; // Slight increase for improving fitness
            case REDUCE_STRESS -> bmr *= 0.95; // Slight reduction for stress reduction
            case IMPROVE_SLEEP -> bmr *= 1.05; // Slight increase for improving sleep
        }

        // Set daily calorie intake based on BMR and activity level
        return switch (activityLevel) {
            case SEDENTARY -> bmr * 1.2;
            case LIGHTLY_ACTIVE -> bmr * 1.375;
            case MODERATELY_ACTIVE -> bmr * 1.55;
            case VERY_ACTIVE -> bmr * 1.725;
        };
    }

    // Calculate Protection Intake
    private Double calculateProteinIntake(HealthInfoDTO healthInfoDTO) {
        double weight = healthInfoDTO.getWeight();

        // Determine the activity factor based on activity level
        double activityFactor = switch (ActivityLevel.valueOf(healthInfoDTO.getActivityLevel())) {
            case SEDENTARY -> 0.8;
            case LIGHTLY_ACTIVE -> 1.0;
            case MODERATELY_ACTIVE -> 1.2;
            case VERY_ACTIVE -> 1.4;
        };

        // Calculate protein intake based on weight and activity factor
        double proteinIntake = weight * activityFactor;

        // Adjust protein intake based on gender
        if (Gender.valueOf(healthInfoDTO.getGender()) == Gender.MALE) {
            proteinIntake *= 0.9;
        }

        return proteinIntake;
    }

    // Calculate recommended Weight
    private Double calculateRecommendedWeight(HealthInfoDTO healthInfoDTO) {
        // Get user-specific data
        Gender gender = Gender.valueOf(healthInfoDTO.getGender());
        Double height = healthInfoDTO.getHeight();
        GoalType goalType = GoalType.valueOf(healthInfoDTO.getGoalType());

        double targetBMI = (goalType == GoalType.LOSE_WEIGHT || goalType == GoalType.BUILD_MUSCLE) ?
                Constants.UPPER_NORMAL_BMI - Constants.MARGIN : (Constants.LOWER_NORMAL_BMI + Constants.UPPER_NORMAL_BMI) / 2.0;

        double recommendedWeight = targetBMI * (height * height);

        if (gender == Gender.FEMALE) {
            recommendedWeight *= 0.95; // Reduce recommended weight by 5% for females
        }

        // Round the recommended weight to one decimal place
        recommendedWeight = Math.round(recommendedWeight * 10.0) / 10.0;

        return recommendedWeight;
    }

    // Calculate Fat Intake
    private Double calculateFatIntake(HealthInfoDTO healthInfoDTO) {
        double weight = healthInfoDTO.getWeight();
        double age = healthInfoDTO.getAge();
        Gender gender = Gender.valueOf(healthInfoDTO.getGender());
        ActivityLevel activityLevel = ActivityLevel.valueOf(healthInfoDTO.getActivityLevel());

        // Constants for fat intake based on activity level (in grams per kg of body weight)
        double sedentaryFat = 0.7;
        double lightlyActiveFat = 0.8;
        double moderatelyActiveFat = 0.9;
        double veryActiveFat = 1.0;

        // Calculate fat intake based on activity level, gender, and age
        double fatIntake = switch (activityLevel) {
            case SEDENTARY -> weight * sedentaryFat;
            case LIGHTLY_ACTIVE -> weight * lightlyActiveFat;
            case MODERATELY_ACTIVE -> weight * moderatelyActiveFat;
            case VERY_ACTIVE -> weight * veryActiveFat;
        };

        // Adjust fat intake based on age and gender
        if (gender == Gender.FEMALE) {
            // Women may require slightly less fat, so reduce it by 10%
            fatIntake *= 0.9;
        }

        // Adjust fat intake based on age (decrease by 1% for each year over 30)
        if (age > 30) {
            fatIntake -= (age - 30) * 0.01 * weight;
        }

        return fatIntake;
    }

    // Calculate Preferred BMR
    private Double calculatePreferredBMR(HealthInfoDTO healthInfoDTO) {
        // Get user's information
        Gender gender = Gender.valueOf(healthInfoDTO.getGender());
        long age = healthInfoDTO.getAge();
        double weightKg = healthInfoDTO.getWeight();
        double heightCm = healthInfoDTO.getHeight();

        // Calculate BMR using the Mifflin-St Jeor Equation
        double bmr = 0.0;

        if (gender == Gender.MALE) {
            bmr = 88.362 + (13.397 * weightKg) + (4.799 * heightCm) - (5.677 * age);
        } else if (gender == Gender.FEMALE) {
            bmr = 447.593 + (9.247 * weightKg) + (3.098 * heightCm) - (4.330 * age);
        }

        // Round the BMR to a reasonable precision
        bmr = Math.round(bmr / 10.0) * 10.0;

        return bmr;
    }

    // Calculate Preferred BMI
    private double calculatePreferredBMI(HealthInfoDTO healthInfoDTO) {
        double bmi = healthInfoDTO.getBmi();
        GoalType goalType = GoalType.valueOf(healthInfoDTO.getGoalType());
        Gender gender = Gender.valueOf(healthInfoDTO.getGender());
        double age = healthInfoDTO.getAge();

        // Calculate preferred BMI based on goal type, gender, age, and other factors
        double preferredBMI = bmi;

        switch (goalType) {
            case LOSE_WEIGHT -> preferredBMI -= 1.0;
            case BUILD_MUSCLE -> preferredBMI += 0.5;
            case IMPROVE_FITNESS -> preferredBMI += 0.3;
            case REDUCE_STRESS -> preferredBMI -= 0.2;
            case IMPROVE_SLEEP -> preferredBMI += 0.2;
        }

        // Adjust preferred BMI based on gender
        if (gender == Gender.MALE) {
            preferredBMI += 0.2;
        } else if (gender == Gender.FEMALE) {
            preferredBMI -= 0.2;
        }

        // Adjust preferred BMI based on age
        if (age < 30) {
            preferredBMI += 0.1;
        } else if (age < 50) {
            preferredBMI -= 0.1;
        } else {
            preferredBMI += 0.2;
        }

        return preferredBMI;
    }

    // Calculate Risk Factor
    private long calculateDiabetesRiskScore(HealthInfoDTO healthInfo) {
        long riskScore = 0;

        // BMI Factor
        if (healthInfo.getBmi() != null && healthInfo.getBmi() > 25) {
            riskScore += 2;
        }

        // Age Factor
        if (healthInfo.getAge() > 45) {
            riskScore += 2;
        }

        // Blood Sugar Level
        if (healthInfo.getSugarLevel() != null && healthInfo.getSugarLevel() > 140) {
            riskScore += 3;
        }

        // Thirst Level
        if ("MORE_THAN_USUAL".equals(healthInfo.getThirstLevel())) {
            riskScore += 1;
        }

        // Frequency of Urination
        if ("MORE_THAN_USUAL".equals(healthInfo.getFrequencyOfUrination())) {
            riskScore += 1;
        }

        // Blurred Vision
        if ("OFTEN".equals(healthInfo.getBlurredVision())) {
            riskScore += 1;
        }

        // Cap risk score at 10
        return Math.min(riskScore, 10);
    }

    // Risk Level
    private RiskLevel determineRiskLevel(long riskScore) {
        if (riskScore >= 7) {
            return RiskLevel.HIGH;
        } else if (riskScore >= 4) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.LOW;
        }
    }

}
