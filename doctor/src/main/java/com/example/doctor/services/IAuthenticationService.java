package com.example.doctor.services;

import com.example.doctor.dto.request.DoctorRegistrationDTO;
import com.example.doctor.dto.response.AuthenticationResponseDTO;
import com.example.doctor.dto.response.RegistrationResponseDTO;

public interface IAuthenticationService {
    public RegistrationResponseDTO registerDoctor(DoctorRegistrationDTO doctorRegistration);
    public long getAuthenticatedUser();
}
