package com.example.doctor.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {
    private long patientId;
    private String patientName;
    private String appointmentType;
    private String imgUrl;
    private LocalTime appointmentStartTime;
    private LocalTime appointmentEndTime;
    private LocalDate appointmentDate;
}
