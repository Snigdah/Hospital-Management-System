package com.example.patient.dto.response;

import com.example.patient.dto.request.PhysicalHealthDTO;
import com.example.patient.model.enums.ActivityLevel;
import com.example.patient.model.enums.BloodGroup;
import com.example.patient.model.enums.Gender;
import com.example.patient.model.enums.GoalType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthProfileDTO {
    private long userId;
    private long age;
    private Double weight;
    private Double height;
    private Double bmi;
    private Double bmr;
    private Gender gender;
    private BloodGroup bloodGroup;
    private GoalType goalType;
    private ActivityLevel activityLevel;
    private PhysicalHealthResponseDTO physicalHealth;
}
