package com.example.doctor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDoctorDTO {
    private String doctorName;
    private String imageUrl;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate appointmentDate;
    private String appointmentType;
}