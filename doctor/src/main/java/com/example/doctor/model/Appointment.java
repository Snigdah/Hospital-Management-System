package com.example.doctor.model;

import com.example.doctor.model.enums.AppointmentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Doctor_Id")
    private long doctorId;

    @Column(name = "PatientId")
    private long patientId;

    @Column(name = "Appointment_Start_Time")
    private LocalTime appointmentStartTime;

    @Column(name = "Appointment_End_Time")
    private LocalTime appointmentEndTime;

    @Column(name = "Appointment_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @Column(name = "Appointment_Type")
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;
}
