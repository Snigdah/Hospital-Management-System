package com.example.patient.dto.response;

import com.example.patient.model.enums.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhysicalHealthResponseDTO {
    private boolean smoke;
    private Double sugarLevel;
    private boolean sinusitis;
    private boolean previousStroke;
    private boolean highCholesterol;
    private boolean chestPain;
    private ThirstLevel thirstLevel;
    private FrequencyOfUrination frequencyOfUrination;
    private BlurredVision blurredVision;
    private AlcoholConsumption alcoholConsumption;
    private BloodPressure bloodPressure;
}
