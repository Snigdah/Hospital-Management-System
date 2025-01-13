package com.example.patient.model;

import com.example.patient.model.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "physical_health")
public class PhysicalHealth {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    private Boolean smoke;
    private Double sugarLevel;
    private Boolean sinusitis;
    private Boolean previousStroke;
    private Boolean highCholesterol;
    private Boolean chestPain;

    @Enumerated(EnumType.STRING) // NORMAL, MORE_THAN_USUAL
    private ThirstLevel thirstLevel;

    @Enumerated(EnumType.STRING) // NORMAL, MORE_THAN_USUAL
    private FrequencyOfUrination frequencyOfUrination;

    @Enumerated(EnumType.STRING) // RARELY, OFTEN
    private BlurredVision blurredVision;

    @Enumerated(EnumType.STRING) //  NONE, OCCASIONAL, MODERATE, HEAVY
    private AlcoholConsumption alcoholConsumption;

    @Enumerated(EnumType.STRING)  //  HIGH, LOW, NORMAL
    private BloodPressure bloodPressure;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_id", nullable = false)
    private HealthProfile healthProfile;
}
