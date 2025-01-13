package com.example.recommendation.entity;

import com.example.recommendation.entity.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recommendation {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    private long userId;

    @Column(name = "daily_calorie_intake")
    private Double dailyCalorieIntake;

    @Column(name = "protein_intake")
    private Double proteinIntake;

    @Column(name = "recommended_weight")
    private Double recommendedWeight;

    @Column(name = "fat_intake")
    private Double fatIntake;

    @Column(name = "preferred_bmr")
    private Double preferredBMR;

    @Column(name = "preferred_bmi")
    private Double preferredBMI;

    private long diabetesRisk;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;
}
