package com.example.patient.dto.response;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfileDTO {
    private long userId;
    private String userName;
    private String imgUrl;
    private String address;
    private String mobile;
    private Date dateOfBirth;
}
