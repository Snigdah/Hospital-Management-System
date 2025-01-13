package com.example.patient.services.impl;

import com.example.patient.dto.request.UserIdDTO;
import com.example.patient.dto.response.PatientCountDTO;
import com.example.patient.dto.response.PatientProfileDTO;
import com.example.patient.dto.response.UserInfoDTO;
import com.example.patient.exception.CustomeException;
import com.example.patient.model.Patient;
import com.example.patient.repository.HealthProfileRepository;
import com.example.patient.repository.PatientRepository;
import com.example.patient.services.IPatientProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientProxyService implements IPatientProxyService {

    private final PatientRepository patientRepository;

    public PatientProxyService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientProfileDTO getPatientProfileByUserId(long userId) {
        // Retrieve the patient profile and validate
        Patient patient = findPatientByUserId(userId);

        log.info("Patient profile retrieved for user id {}", userId);
        return convertToPatientProfileDTO(patient);
    }

    // Check if user exists or not
    private Patient findPatientByUserId(long userId) {
        return patientRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new CustomeException(HttpStatus.BAD_REQUEST, "User not found");
                });
    }

    // Fetch List of User Info
    public List<UserInfoDTO> getUserInfoList(List<UserIdDTO> userIdDTOs) {
        log.info("Fetching user info for provided user IDs");

        // Convert UserIdDTO list to userId list
        List<Long> userIds = userIdDTOs.stream()
                .map(UserIdDTO::getUserId)
                .collect(Collectors.toList());

        // Find patients
        List<Patient> patients = patientRepository.findByUserIdInOrderByUserIdAsc(userIds);
        log.debug("Number of patients found: {}", patients.size());


        // Convert to UserInfoDTO list
        return patients.stream().map(patient ->
                UserInfoDTO.builder()
                        .userId(patient.getUserId())
                        .userName(patient.getUserName())
                        .imgUrl(patient.getImgUrl())
                        .build()
        ).collect(Collectors.toList());
    }

    // Count patient number and donor number
    public PatientCountDTO getPatientCount(){
        PatientCountDTO patientCount = new PatientCountDTO();
        patientCount.setTotalPatient(patientRepository.count());
        patientCount.setTotalDonor(patientRepository.countByInterestedToBloodDonate(true));
        return patientCount;
    }

    // Convert Entity to DTO
    private PatientProfileDTO convertToPatientProfileDTO(Patient patient) {
        return PatientProfileDTO.builder()
                .userId(patient.getUserId())
                .userName(patient.getUserName())
                .imgUrl(patient.getImgUrl())
                .address(patient.getAddress())
                .mobile(patient.getMobile())
                .dateOfBirth(patient.getDateOfBirth())
                .build();
    }
}
