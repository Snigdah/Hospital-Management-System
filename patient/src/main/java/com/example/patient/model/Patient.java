package com.example.patient.model;

import com.example.patient.model.enums.BloodGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_Id")
    private long userId;

    @Column(length = 255)
    private String userName;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Enumerated(EnumType.STRING)  //  A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE
    private BloodGroup bloodGroup;

    private boolean interestedToBloodDonate;

    @Column(name = "Date_of_birth")
    private Date dateOfBirth;
}
