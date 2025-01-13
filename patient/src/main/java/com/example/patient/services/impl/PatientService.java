package com.example.patient.services.impl;

import com.example.patient.dto.response.HealthProfileDTO;
import com.example.patient.dto.response.PatientProfileDTO;
import com.example.patient.dto.response.PhysicalHealthResponseDTO;
import com.example.patient.exception.CustomeException;
import com.example.patient.model.HealthProfile;
import com.example.patient.model.Patient;
import com.example.patient.model.PhysicalHealth;
import com.example.patient.repository.HealthProfileRepository;
import com.example.patient.repository.PatientRepository;
import com.example.patient.services.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientService implements IPatientService {
    private final PatientRepository patientRepository;
    private final HealthProfileRepository healthProfileRepository;

    public PatientService(PatientRepository patientRepository, HealthProfileRepository healthProfileRepository) {
        this.patientRepository = patientRepository;
        this.healthProfileRepository = healthProfileRepository;
    }

    // Get Patient Profile information
    @Override
    public PatientProfileDTO getPatientProfileByUserId(long userId, long loginUserId) {
        validateAccess(userId, loginUserId);

        Patient patient = findPatientByUserId(userId);

        log.info("Patient profile retrieved for user id {}", userId);
        return convertToPatientProfileDTO(patient);
    }

    // Get List of Users profiles information
    @Override
    public List<PatientProfileDTO> getAllUserInformation() {

        // Retrieve all patients' profiles and convert to DTOs
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(this::convertToPatientProfileDTO)
                .collect(Collectors.toList());
    }

    // Get User Health Information by User ID
    public HealthProfileDTO getHealthInfoByUserId(long userId) {

        // Retrieve User Health Information and convert to DTOs
        HealthProfile healthProfile = getHealthProfileByUserId(userId);
        return mapHealthProfileToDTO(healthProfile);
    }

    // Validate if user Exists or not
    private HealthProfile getHealthProfileByUserId(long userId) {
        return healthProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Health profile not found for user with userId: {}", userId);
                    return new CustomeException(HttpStatus.NOT_FOUND, "Health profile not found for user");
                });
    }

    // Check if user exists or not
    private Patient findPatientByUserId(long userId) {
        return patientRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new CustomeException(HttpStatus.BAD_REQUEST, "User not found");
                });
    }

    // Validate is authenticated user try or not
    private void validateAccess(long userId, long loginUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isPatientRole(authentication) && userId != loginUserId) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Patient can't access another user's health information");
        }
    }

    // Check is role Patient or not
    private boolean isPatientRole(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"));
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

    // Map Entity to DTO
    private HealthProfileDTO mapHealthProfileToDTO(HealthProfile healthProfile) {
        HealthProfileDTO healthProfileDTO = new HealthProfileDTO();
        healthProfileDTO.setUserId(healthProfile.getUserId());
        healthProfileDTO.setAge(healthProfile.getAge());
        healthProfileDTO.setWeight(healthProfile.getWeight());
        healthProfileDTO.setHeight(healthProfile.getHeight());
        healthProfileDTO.setBmi(healthProfile.getBmi());
        healthProfileDTO.setBmr(healthProfile.getBmr());
        healthProfileDTO.setGender(healthProfile.getGender());
        healthProfileDTO.setBloodGroup(healthProfile.getBloodGroup());
        healthProfileDTO.setGoalType(healthProfile.getGoalType());
        healthProfileDTO.setActivityLevel(healthProfile.getActivityLevel());

        PhysicalHealthResponseDTO physicalHealthResponseDTO = mapPhysicalHealthToDTO(healthProfile.getPhysicalHealth());
        healthProfileDTO.setPhysicalHealth(physicalHealthResponseDTO);

        return healthProfileDTO;
    }

    // Map entity to DTO
    private PhysicalHealthResponseDTO mapPhysicalHealthToDTO(PhysicalHealth physicalHealth) {
        PhysicalHealthResponseDTO physicalHealthResponseDTO = new PhysicalHealthResponseDTO();
        physicalHealthResponseDTO.setSmoke(physicalHealth.getSmoke());
        physicalHealthResponseDTO.setSugarLevel(physicalHealth.getSugarLevel());
        physicalHealthResponseDTO.setSinusitis(physicalHealth.getSinusitis());
        physicalHealthResponseDTO.setPreviousStroke(physicalHealth.getPreviousStroke());
        physicalHealthResponseDTO.setHighCholesterol(physicalHealth.getHighCholesterol());
        physicalHealthResponseDTO.setChestPain(physicalHealth.getChestPain());
        physicalHealthResponseDTO.setThirstLevel(physicalHealth.getThirstLevel());
        physicalHealthResponseDTO.setFrequencyOfUrination(physicalHealth.getFrequencyOfUrination());
        physicalHealthResponseDTO.setBlurredVision(physicalHealth.getBlurredVision());
        physicalHealthResponseDTO.setAlcoholConsumption(physicalHealth.getAlcoholConsumption());
        physicalHealthResponseDTO.setBloodPressure(physicalHealth.getBloodPressure());

        return physicalHealthResponseDTO;
    }

    // Search Patient by Patient Blood Group
    @Override
    public List<Patient> searchPatients(String bloodGroup, String sortBy, Sort.Direction sortDir) {
        Sort sort = Sort.by(sortDir, sortBy);
        Specification<Patient> spec = PatientSpecification.withDynamicQuery(bloodGroup);
        return patientRepository.findAll(spec, sort);
    }
}
