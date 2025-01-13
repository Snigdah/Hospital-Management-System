package com.example.patient.services;

import com.example.patient.dto.response.HealthProfileDTO;
import com.example.patient.dto.response.PatientProfileDTO;
import com.example.patient.model.Patient;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IPatientService {
    public PatientProfileDTO getPatientProfileByUserId(long userId, long loginUserId);
    public List<PatientProfileDTO> getAllUserInformation();
    public HealthProfileDTO getHealthInfoByUserId(long userId);
    public List<Patient> searchPatients(String bloodGroup, String sortBy, Sort.Direction sortDir);
}
