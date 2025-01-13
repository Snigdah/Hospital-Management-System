package com.example.notification.model;

import com.example.notification.model.enums.Role;
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
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_Id")
    private long userId;

    @Column(name = "Appointment_Time")
    private LocalTime appointmentTime;

    @Column(name = "Appointment_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    private Role role;
}
