package com.example.community.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hospital_review")
public class HospitalReview {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    private double ratting;

    @Column(name = "review_message", nullable = false, columnDefinition = "TEXT")
    private String reviewMessage;
}
