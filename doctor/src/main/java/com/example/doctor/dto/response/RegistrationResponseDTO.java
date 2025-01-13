package com.example.doctor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseDTO {
    private long userId;
    private String role;
    private String token;
    private long roomNumber;
    private LocalTime startTime;
    private LocalTime endTime;
}
