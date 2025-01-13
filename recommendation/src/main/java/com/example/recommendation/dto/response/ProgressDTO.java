package com.example.recommendation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressDTO {
    private long id;
    private long userId;
    private double bmi;
    private double bmr;
    private double sugarLevel;
    private LocalDate date;
}
