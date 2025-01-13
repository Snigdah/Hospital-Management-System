package com.example.patient.utils;

import com.example.patient.exception.CustomeException;
import com.example.patient.model.enums.*;
import org.springframework.http.HttpStatus;

public class EnumValidation {
    public static ActivityLevel parseActivityLevel(String userActivityLevel) {
        try {
            return ActivityLevel.valueOf(userActivityLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid activity level. Supported activity levels are SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, and VERY_ACTIVE.");
        }
    }

    public static AlcoholConsumption parseAlcoholConsumption(String alcoholConsumptionValue) {
        try {
            return AlcoholConsumption.valueOf(alcoholConsumptionValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid alcohol consumption level. Supported levels are NONE, OCCASIONAL, MODERATE, HEAVY.");
        }
    }

    public static BloodGroup parseBloodGroup(String userBloodGroup) {
        try {
            return BloodGroup.valueOf(userBloodGroup.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid blood group. Supported blood groups are A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, and O_NEGATIVE.");
        }
    }

    public static BloodPressure parseBloodPressure(String bloodPressureValue) {
        try {
            return BloodPressure.valueOf(bloodPressureValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid blood pressure level. Supported levels are  HIGH, LOW, NORMAL.");
        }
    }

    public static BlurredVision parseBlurredVision(String blurredVision) {
        try {
            return BlurredVision.valueOf(blurredVision.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid blurred Vision level. Supported levels are  RARELY, OFTEN .");
        }
    }

    public static FrequencyOfUrination parseFrequencyOfUrination(String frequencyOfUrination) {
        try {
            return FrequencyOfUrination.valueOf(frequencyOfUrination.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid Frequency Of Urination level. Supported levels are  NORMAL, MORE_THAN_USUAL .");
        }
    }

    public static Gender parseGender(String userGender) {
        try {
            return Gender.valueOf(userGender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid gender. Supported genders are MALE and FEMALE.");
        }
    }

    public static GoalType parseGoalType(String userGoalType) {
        try {
            return GoalType.valueOf(userGoalType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid goal type. Supported goal types are LOSE_WEIGHT, BUILD_MUSCLE, IMPROVE_FITNESS, REDUCE_STRESS, and IMPROVE_SLEEP.");
        }
    }

    public static ThirstLevel parseThirstLevel(String thirstLevel) {
        try {
            return ThirstLevel.valueOf(thirstLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid Thirst Level. Supported Thirst types are NORMAL, MORE_THAN_USUAL");
        }
    }
}
