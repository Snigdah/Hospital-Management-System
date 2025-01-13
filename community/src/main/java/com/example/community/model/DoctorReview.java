package com.example.community.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doctor_review")
public class DoctorReview {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "doctor_id", nullable = false)
    private long doctorId;

    @Column(name = "review_message", nullable = false, columnDefinition = "TEXT")
    private String reviewMessage;
}
