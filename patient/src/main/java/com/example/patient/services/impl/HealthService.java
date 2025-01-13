package com.example.patient.services.impl;

import com.example.patient.dto.request.HealthInfoDTO;
import com.example.patient.dto.request.HealthProfileDTO;
import com.example.patient.dto.request.PhysicalHealthDTO;
import com.example.patient.exception.CustomeException;
import com.example.patient.model.HealthProfile;
import com.example.patient.model.PhysicalHealth;
import com.example.patient.repository.HealthProfileRepository;
import com.example.patient.services.IHealthService;
import com.example.patient.utils.Constants;
import com.example.patient.utils.EnumValidation;
import com.example.patient.webClient.IRecommendationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HealthService implements IHealthService {
    private final HealthProfileRepository healthProfileRepository;
    private final IRecommendationClient recommendationClient;

    public HealthService(HealthProfileRepository healthProfileRepository, IRecommendationClient recommendationClient) {
        this.healthProfileRepository = healthProfileRepository;
        this.recommendationClient = recommendationClient;
    }

    // Create Health Profile
    public void createHealthProfile(long userId, HealthProfileDTO healthProfileDTO) {
        // Validate user already exists or not
        if(healthProfileRepository.findByUserId(userId).isPresent()){
            throw new CustomeException(HttpStatus.BAD_REQUEST,
                    "You already create your health profile, You can only update");
        }

        mapToHealthProfile(healthProfileDTO, new HealthProfile(), userId);
    }

    // Update Health Profile
    public void updateHealthProfile(long userId, HealthProfileDTO healthProfileDTO) {
        // Validate the health profile already exists or not
        HealthProfile healthProfile = healthProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomeException(HttpStatus.BAD_REQUEST,
                        "You didn't update your health profile, Please create first"));

        mapToHealthProfile(healthProfileDTO, healthProfile, userId);
    }


    // Map DTO to Entity
    private void mapToHealthProfile(HealthProfileDTO healthProfileDTO, HealthProfile healthProfile, long userId) {
        PhysicalHealth physicalHealth = healthProfile.getPhysicalHealth() == null ?
                new PhysicalHealth() : healthProfile.getPhysicalHealth();
        physicalHealth = mapToPhysicalHealth(healthProfileDTO.getPhysicalHealth(), physicalHealth);

        healthProfile.setUserId(userId);
        healthProfile.setAge(healthProfileDTO.getAge());
        healthProfile.setWeight(healthProfileDTO.getWeight());
        healthProfile.setHeight(healthProfileDTO.getHeight());
        healthProfile.setGender(EnumValidation.parseGender(healthProfileDTO.getGender()));
        healthProfile.setBloodGroup(EnumValidation.parseBloodGroup(healthProfileDTO.getBloodGroup()));
        healthProfile.setGoalType(EnumValidation.parseGoalType(healthProfileDTO.getGoalType()));
        healthProfile.setActivityLevel(EnumValidation.parseActivityLevel(healthProfileDTO.getActivityLevel()));
        healthProfile.setBmr(calculateBmr(healthProfileDTO));
        healthProfile.setBmi(calculateBmi(healthProfileDTO));
        healthProfile.setPhysicalHealth(physicalHealth);
        physicalHealth.setHealthProfile(healthProfile);

        healthProfileRepository.save(healthProfile);
        sendDataRecommendationService(healthProfile);
    }

    // Convert Entity to DTO
    private void sendDataRecommendationService(HealthProfile healthProfile) {
            HealthInfoDTO info = HealthInfoDTO
                .builder()
                .userId(healthProfile.getUserId())
                .age(healthProfile.getAge())
                .weight(healthProfile.getWeight())
                .height(healthProfile.getHeight())
                .bmi(healthProfile.getBmi())
                .bmr(healthProfile.getBmr())
                .gender(healthProfile.getGender().toString())
                .sugarLevel(healthProfile.getPhysicalHealth().getSugarLevel())
                .sinusitis(healthProfile.getPhysicalHealth().getSinusitis())
                .previousStroke(healthProfile.getPhysicalHealth().getPreviousStroke())
                .thirstLevel(healthProfile.getPhysicalHealth().getThirstLevel().toString())
                .frequencyOfUrination(healthProfile.getPhysicalHealth().getFrequencyOfUrination().toString())
                .blurredVision(healthProfile.getPhysicalHealth().getBlurredVision().toString())
                .alcoholConsumption(healthProfile.getPhysicalHealth().getAlcoholConsumption().toString())
                .bloodGroup(healthProfile.getBloodGroup().toString())
                .goalType(healthProfile.getGoalType().toString())
                .activityLevel(healthProfile.getActivityLevel().toString())
                .build();
        
        sentRecommendationMicroservice(info);
    }

    // Send data to Recommendation Microservice
    private void sentRecommendationMicroservice(HealthInfoDTO info) {
        recommendationClient.importUserHealthData(info)
                .subscribe(
                        response -> log.info("Data received successfully by Recommendation Microservice"),
                        ex -> log.error("Failed to Send data to Recommendation Microservice: " + ex.getMessage())
                );
    }

    // Map to DTO
    private PhysicalHealth mapToPhysicalHealth(PhysicalHealthDTO physicalHealthDTO, PhysicalHealth physicalHealth) {
        physicalHealth.setSmoke(physicalHealthDTO.getSmoke());
        physicalHealth.setSugarLevel(physicalHealthDTO.getSugarLevel());
        physicalHealth.setSinusitis(physicalHealthDTO.getSinusitis());
        physicalHealth.setPreviousStroke(physicalHealthDTO.getPreviousStroke());
        physicalHealth.setHighCholesterol(physicalHealthDTO.getHighCholesterol());
        physicalHealth.setBlurredVision(EnumValidation.parseBlurredVision(physicalHealthDTO.getBlurredVision()));
        physicalHealth.setChestPain(physicalHealthDTO.getChestPain());
        physicalHealth.setThirstLevel(EnumValidation.parseThirstLevel(physicalHealthDTO.getThirstLevel()));
        physicalHealth.setFrequencyOfUrination(EnumValidation.parseFrequencyOfUrination(physicalHealthDTO.getFrequencyOfUrination()));
        physicalHealth.setBloodPressure(EnumValidation.parseBloodPressure(physicalHealthDTO.getBloodPressure()));
        physicalHealth.setAlcoholConsumption(EnumValidation.parseAlcoholConsumption(physicalHealthDTO.getAlcoholConsumption()));
        physicalHealth.setBloodPressure(EnumValidation.parseBloodPressure(physicalHealthDTO.getBloodPressure()));

        return physicalHealth;
    }

    // Calculate BMI
    private Double calculateBmi(HealthProfileDTO healthDetailsDto) {
        Double weight = healthDetailsDto.getWeight();
        Double height = healthDetailsDto.getHeight();
        // BMI formula: weight (kg) / (height (m) * height (m))
        return weight / (height * height);
    }

    // Calculate BMR
    private Double calculateBmr(HealthProfileDTO healthDetailsDto) {
        Double weight = healthDetailsDto.getWeight();
        Double height = healthDetailsDto.getHeight();
        String gender = healthDetailsDto.getGender();
        long age = healthDetailsDto.getAge();

        if (gender.equalsIgnoreCase("Male")) {
            // BMR formula for men (Harris-Benedict Equation):
            // BMR = 88.362 + (13.397 * weight in kg) + (4.799 * height in cm) - (5.677 * age in years)
            return Constants.MALE_BMR_CONSTANT + (Constants.MALE_BMR_WEIGHT_COEFFICIENT * weight)
                    + (Constants.MALE_BMR_HEIGHT_COEFFICIENT * height * 100) - (Constants.MALE_BMR_AGE_COEFFICIENT * age);
        } else {
            // BMR formula for women (Harris-Benedict Equation):
            // BMR = 447.593 + (9.247 * weight in kg) + (3.098 * height in cm) - (4.330 * age in years)
            return Constants.FEMALE_BMR_CONSTANT + (Constants.FEMALE_BMR_WEIGHT_COEFFICIENT * weight)
                    + (Constants.FEMALE_BMR_HEIGHT_COEFFICIENT * height * 100) - (Constants.FEMALE_BMR_AGE_COEFFICIENT * age);
        }
    }
}
