package com.example.patient.services.impl;

import com.example.patient.dto.request.PatientRegisterDTO;
import com.example.patient.dto.request.RegisterRequestDTO;
import com.example.patient.dto.response.AuthenticationResponseDTO;
import com.example.patient.exception.AuthenticationException;
import com.example.patient.exception.FeignCustomException;
import com.example.patient.feingClient.SecurityServiceClient;
import com.example.patient.model.Patient;
import com.example.patient.repository.PatientRepository;
import com.example.patient.services.IAuthenticationService;
import com.example.patient.utils.Constants;
import com.example.patient.utils.EnumValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService implements IAuthenticationService {
    private final PatientRepository patientRepository;
    private final SecurityServiceClient securityServiceClient;

    public AuthenticationService(PatientRepository patientRepository, SecurityServiceClient securityServiceClient) {
        this.patientRepository = patientRepository;
        this.securityServiceClient = securityServiceClient;
    }

    /**
     * Orchestrates the patient registration process by creating a user account
     * and saving the patient details in the repository.
     *
     * @param patientRegisterDTO Data transfer object containing patient registration details.
     * @return AuthenticationResponseDTO Response data transfer object containing authentication details.
     */
    @Override
    public AuthenticationResponseDTO registerPatient(PatientRegisterDTO patientRegisterDTO) {
        EnumValidation.parseBloodGroup(patientRegisterDTO.getBloodGroup());
        AuthenticationResponseDTO registerResponse = createAccount(patientRegisterDTO);
        savePatient(patientRegisterDTO, registerResponse);
        return registerResponse;
    }

    // Call Security service for registration
    private AuthenticationResponseDTO createAccount(PatientRegisterDTO patientRegisterDTO) {
        RegisterRequestDTO requestData = buildRegisterRequestDTO(patientRegisterDTO);
        try {
            log.info("Calling security service for registration");
            return securityServiceClient.register(requestData);
        } catch (FeignCustomException ex) {
            log.error("An error occurred during account creation: {}", ex.getMessage());
            throw new AuthenticationException(ex.getStatusCode(), ex.getErrorDetails().getMessage());
        }
    }

    // Save Patient information in DB
    private void savePatient(PatientRegisterDTO patientRegisterDTO, AuthenticationResponseDTO registerResponse) {
        Patient patient = buildPatient(patientRegisterDTO, registerResponse.getUserId());
        patientRepository.save(patient);
        log.info("Patient with ID {} has been registered successfully", patient.getId());
    }

    // Map Registration DTO from Patient DTO
    private RegisterRequestDTO buildRegisterRequestDTO(PatientRegisterDTO patientRegisterDTO) {
        return RegisterRequestDTO.builder()
                .email(patientRegisterDTO.getEmail())
                .password(patientRegisterDTO.getPassword())
                .role(Constants.ROLE_PATIENT)
                .build();
    }

    // Map to DTO to Entity
    private Patient buildPatient(PatientRegisterDTO patientRegisterDTO, Long userId) {
        return Patient.builder()
                .userId(userId)
                .userName(patientRegisterDTO.getUserName())
                .imgUrl(patientRegisterDTO.getImgUrl())
                .address(patientRegisterDTO.getAddress())
                .mobile(patientRegisterDTO.getMobile())
                .dateOfBirth(patientRegisterDTO.getDateOfBirth())
                .bloodGroup(EnumValidation.parseBloodGroup(patientRegisterDTO.getBloodGroup()))
                .interestedToBloodDonate(patientRegisterDTO.getInterestedToBloodDonate())
                .build();
    }


    // Get userId from Principal
    public long getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return Long.parseLong(authentication.getName());
        } else {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
    }
}
