package com.example.patient.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRegisterDTO {
    @NotEmpty(message = "User Name should not be null or empty")
    private String userName;

    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be null or empty")
    private String password;

    @NotEmpty(message = "Image URL should not be null or empty")
    private String imgUrl;

    @NotEmpty(message = "Address should not be null or empty")
    private String address;

    @NotEmpty(message = "Mobile should not be null or empty")
    private String mobile;

    @NotEmpty(message = "Blood group should not be null or empty")
    private String bloodGroup;

    @NotNull(message = "Blood donate should not be null or empty")
    private Boolean interestedToBloodDonate;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;
}
