package com.example.patient.dto.request;

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
public class PhysicalHealthDTO {
    @NotNull
    private Boolean smoke;

    @NotNull
    @Min(value = 0, message = "Sugar Level should be negative and can't be empty or null")
    private Double sugarLevel;

    @NotNull
    private Boolean sinusitis;

    @NotNull
    private Boolean previousStroke;

    @NotNull
    private Boolean highCholesterol;

    @NotNull
    private Boolean chestPain;

    @NotEmpty(message = "Gender shouldn't be empty or null")
    private String thirstLevel;

    @NotEmpty(message = "FrequencyOfUrination shouldn't be empty or null")
    private String frequencyOfUrination;

    @NotEmpty(message = "Gender shouldn't be empty or null")
    private String blurredVision;

    @NotEmpty(message = "Gender shouldn't be empty or null")
    private String alcoholConsumption;

    @NotEmpty(message = "Gender shouldn't be empty or null")
    private String bloodPressure;
}
