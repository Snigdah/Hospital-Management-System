package com.example.doctor.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDTO {
    @NotNull
    @Min(value = 0, message = "Doctor Id should not be negative or empty or null")
    private long doctorId;

    private LocalDate appointmentDate;
    private LocalTime appointmentStartTime;

    @NotEmpty(message = "Department should not be null or empty")
    private String appointmentType;
}
