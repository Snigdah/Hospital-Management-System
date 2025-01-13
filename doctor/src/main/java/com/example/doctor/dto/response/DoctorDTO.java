package com.example.doctor.dto.response;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private long id;
    private long userId;
    private long deptId;
    private long roomId;
    private String doctorName;
    private String imgUrl;
    private long yearOfExp;
    private LocalTime startTime;
    private LocalTime endTime;
    private long dailyPatientCap;
    private String phone;
    private String medicalName;
    private boolean active;
    private String medicalDegree;
    private String medicalDesignation;
    private String department;
}
