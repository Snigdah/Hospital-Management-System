package com.example.patient.model;

import com.example.patient.model.enums.ActivityLevel;
import com.example.patient.model.enums.BloodGroup;
import com.example.patient.model.enums.Gender;
import com.example.patient.model.enums.GoalType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "health_profile")
public class HealthProfile {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "age", nullable = false)
    private long age;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "bmi", nullable = false)
    private Double bmi;

    @Column(name = "bmr", nullable = false)
    private Double bmr;

    @Enumerated(EnumType.STRING)  // MALE, FEMALE
    private Gender gender;

    @Enumerated(EnumType.STRING)  //  A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)  // LOSE_WEIGHT, BUILD_MUSCLE, IMPROVE_FITNESS, REDUCE_STRESS, IMPROVE_SLEEP
    private GoalType goalType;

    @Enumerated(EnumType.STRING)  // SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE
    private ActivityLevel activityLevel;

    @OneToOne(mappedBy = "healthProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private PhysicalHealth physicalHealth;
}
