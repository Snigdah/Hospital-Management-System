package com.example.patient.services;

import com.example.patient.dto.request.PatientRegisterDTO;
import com.example.patient.dto.response.AuthenticationResponseDTO;

public interface IAuthenticationService {
    public AuthenticationResponseDTO registerPatient(PatientRegisterDTO patientRegisterDTO);
    public long getAuthenticatedUser();
}
