package com.example.doctor.dto.request;

import com.example.doctor.model.enums.MedicalDegree;
import com.example.doctor.model.enums.MedicalDesignation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRegistrationDTO {
    @NotEmpty(message = "Doctor Name should not be null or empty")
    private String doctorName;

    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be null or empty")
    private String password;

    @NotEmpty(message = "Department should not be null or empty")
    private String deptName;

    @NotEmpty(message = "Image URL should not be null or empty")
    private String imgUrl;

    //@NotEmpty(message = "Image URL should not be null or empty")
    private LocalTime startTime;

    @NotNull
    @Min(value = 1, message = "Year of Exp should not be negative or zero or empty or null")
    private Long dailyPatientCap;

    @NotNull
    @Min(value = 0, message = "Year of Exp should not be negative or empty or null")
    private Long yearOfExp;

    @NotEmpty(message = "Medicine should not be null or empty")
    private String medicalName;

    @NotEmpty(message = "Phone should not be null or empty")
    private String phone;

    @NotEmpty(message = "Medical Designation should not be null or empty")
    private String medicalDesignation;

    @NotEmpty(message = "Medical Degree should not be null or empty")
    private String medicalDegree;
}
