package com.example.patient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthProfileDTO {
    @NotNull
    @Min(value = 1, message = "Age should be between 1 and can't be empty or null")
    private long age;

    @NotNull
    @Min(value = 1, message = "Weight should be min 1 and can't be empty or null")
    private Double weight;

    @NotNull
    @Min(value = 1, message = "Height should be between 1 and can't be empty or null")
    private Double height;

    @NotEmpty(message = "Gender shouldn't be empty or null")
    private String gender;

    @NotEmpty(message = "Blood Group shouldn't be empty or null")
    private String bloodGroup;

    @NotEmpty(message = "Goal type shouldn't be empty or null")
    private String goalType;

    @NotEmpty(message = "Activity level shouldn't be empty or null")
    private String activityLevel;

    @Valid
    private PhysicalHealthDTO physicalHealth;
}
