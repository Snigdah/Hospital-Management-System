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
public class AvailableSlotResponseDTO {
    private LocalTime appointmentStartTime;
    private LocalTime appointmentEndTime;
}
