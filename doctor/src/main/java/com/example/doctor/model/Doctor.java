package com.example.doctor.model;

import com.example.doctor.model.enums.MedicalDegree;
import com.example.doctor.model.enums.MedicalDesignation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_Id")
    private long userId;

    @Column(name = "dept_Id")
    private long deptId;

    @Column(name = "room_id")
    private long roomId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "Year_of_Exp")
    private long yearOfExp;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "daily_patient_cap")
    private long dailyPatientCap;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    private MedicalDesignation medicalDesignation;

    @Enumerated(EnumType.STRING)
    private MedicalDegree medicalDegree;

    private String department;

    @Column(name = "medical_name")
    private String medicalName;

    private boolean active = false;
}
