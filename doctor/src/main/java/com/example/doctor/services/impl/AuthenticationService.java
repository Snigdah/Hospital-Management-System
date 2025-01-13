package com.example.doctor.services.impl;

import com.example.doctor.dto.request.CreateDoctorDTO;
import com.example.doctor.dto.request.DoctorRegistrationDTO;
import com.example.doctor.dto.request.RegisterRequestDTO;
import com.example.doctor.dto.request.RoomAvailableDTO;
import com.example.doctor.dto.response.AuthenticationResponseDTO;
import com.example.doctor.dto.response.RegistrationResponseDTO;
import com.example.doctor.dto.response.ResponseDoctorDTO;
import com.example.doctor.exception.AuthenticationException;
import com.example.doctor.exception.CustomeException;
import com.example.doctor.exception.FeignCustomException;
import com.example.doctor.feingClient.AdminServiceClient;
import com.example.doctor.feingClient.SecurityServiceClient;
import com.example.doctor.model.Doctor;
import com.example.doctor.model.enums.MedicalDegree;
import com.example.doctor.model.enums.MedicalDesignation;
import com.example.doctor.repository.DoctorRepository;
import com.example.doctor.services.IAuthenticationService;
import com.example.doctor.utils.Constants;
import com.example.doctor.utils.EnumValidation;
import com.example.doctor.webclient.IAdministrativeServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class AuthenticationService implements IAuthenticationService {

    private final DoctorRepository doctorRepository;
    private final AdminServiceClient adminServiceClient;
    private final SecurityServiceClient securityServiceClient;
    private final IAdministrativeServiceClient administrativeServiceClient;

    public AuthenticationService(DoctorRepository doctorRepository, AdminServiceClient adminServiceClient, SecurityServiceClient securityServiceClient, IAdministrativeServiceClient administrativeServiceClient) {
        this.doctorRepository = doctorRepository;
        this.adminServiceClient = adminServiceClient;
        this.securityServiceClient = securityServiceClient;
        this.administrativeServiceClient = administrativeServiceClient;
    }

    // Registration Doctor
    @Override
    public RegistrationResponseDTO registerDoctor(DoctorRegistrationDTO doctorRegistration) {
        LocalTime endTime = calculateEndTime(doctorRegistration.getStartTime(), doctorRegistration.getDailyPatientCap());

        // Validate DTO
        validateDTO(doctorRegistration);

        // Validate Dept, Assign room and Registration
        ResponseDoctorDTO responseDoctorDTO = handleRoomAndDepartmentAssignment(doctorRegistration);
        AuthenticationResponseDTO registrationResponse = handleUserRegistration(doctorRegistration, responseDoctorDTO);

        // Calculate the end time based on the start time and daily patient cap
        Doctor doctor = buildDoctor(doctorRegistration, registrationResponse, responseDoctorDTO, endTime);
        doctorRepository.save(doctor);

        return buildRegistrationResponse(registrationResponse, responseDoctorDTO, doctorRegistration, endTime);
    }

    private void validateDTO(DoctorRegistrationDTO doctorRegistration) {
        EnumValidation.parseMedicalDegree(doctorRegistration.getMedicalDegree());
        EnumValidation.parseMedicalDesignation(doctorRegistration.getMedicalDesignation());
    }

    // Handles assignment of room and department to the doctor
    private ResponseDoctorDTO handleRoomAndDepartmentAssignment(DoctorRegistrationDTO doctorRegistration) {
        log.debug("Assigning room and department for doctor.");
        try {
            CreateDoctorDTO doctorDTO = CreateDoctorDTO.builder().deptName(doctorRegistration.getDeptName()).build();
            return adminServiceClient.assignDoctorDeptAndRoom(doctorDTO);
        } catch (FeignCustomException ex) {
            log.error("Error during room and department assignment", ex);
            throw new AuthenticationException(ex.getStatusCode(), ex.getErrorDetails().getMessage());
        }
    }

    // Performs a rollback in case of failure during the registration process 
    private AuthenticationResponseDTO handleUserRegistration(DoctorRegistrationDTO doctorRegistration, ResponseDoctorDTO responseDoctorDTO) {
        log.debug("Registering doctor in security service.");
        try {
            RegisterRequestDTO registerRequest = buildRegisterRequest(doctorRegistration);
            return securityServiceClient.register(registerRequest);
        } catch (FeignCustomException ex) {
            log.error("Error during user registration", ex);
            performRollBack(responseDoctorDTO.getRoomId()); // Pass the room ID from responseDoctorDTO
            throw new AuthenticationException(ex.getStatusCode(), ex.getErrorDetails().getMessage());
        }
    }

    // Performs a rollback in case of failure during the registration process
    private void performRollBack(long roomId) {
        log.debug("Rollback initiated for room ID: {}", roomId);
        RoomAvailableDTO rollBackRequest = RoomAvailableDTO.builder().roomId(roomId).build();
        sendToAdminMicroservice(rollBackRequest);
    }

    // Map DTO to Entity for Registration
    private RegisterRequestDTO buildRegisterRequest(DoctorRegistrationDTO doctorRegistration) {
        return RegisterRequestDTO.builder()
                .email(doctorRegistration.getEmail())
                .password(doctorRegistration.getPassword())
                .role(Constants.ROLE_DOCTOR)
                .build();
    }

    // Builds the Doctor entity from the DTO and response data
    private Doctor buildDoctor(DoctorRegistrationDTO doctorRegistration, AuthenticationResponseDTO registrationResponse,
                               ResponseDoctorDTO responseDoctorDTO, LocalTime endTime) {
        return Doctor.builder()
                .userId(registrationResponse.getUserId())
                .deptId(responseDoctorDTO.getDeptId())
                .roomId(responseDoctorDTO.getRoomId())
                .doctorName(doctorRegistration.getDoctorName())
                .imgUrl(doctorRegistration.getImgUrl())
                .yearOfExp(doctorRegistration.getYearOfExp())
                .startTime(doctorRegistration.getStartTime())
                .endTime(endTime)
                .dailyPatientCap(doctorRegistration.getDailyPatientCap())
                .phone(doctorRegistration.getPhone())
                .medicalName(doctorRegistration.getMedicalName())
                .department(doctorRegistration.getDeptName())
                .medicalDegree(MedicalDegree.valueOf(doctorRegistration.getMedicalDegree()))
                .medicalDesignation(MedicalDesignation.valueOf(doctorRegistration.getMedicalDesignation()))
                .build();
    }

    // Communicates with the administrative microservice for room availability updates
    private RegistrationResponseDTO buildRegistrationResponse(AuthenticationResponseDTO registrationResponse,
                                                              ResponseDoctorDTO responseDoctorDTO, DoctorRegistrationDTO doctorRegistration,
                                                              LocalTime endTime) {
        return RegistrationResponseDTO.builder()
                .userId(registrationResponse.getUserId())
                .role(registrationResponse.getRole())
                .token(registrationResponse.getToken())
                .roomNumber(responseDoctorDTO.getRoomNumber())
                .startTime(doctorRegistration.getStartTime())
                .endTime(endTime)
                .build();
    }

    // Communicates with the administrative microservice for room availability ROLL BACK using REACTIVE Programing
    private void sendToAdminMicroservice(RoomAvailableDTO roomAvailableDTO){
        administrativeServiceClient.changeRoomAvailability(roomAvailableDTO)
                .subscribe(
                        response -> log.info("Data received successfully by Admin Microservice"),
                        ex -> log.error("Failed to import to Admin Microservice: " + ex.getMessage())
                );
    }


    // Calculates the end time for doctor's appointments based on start time and patient capacity
    private LocalTime calculateEndTime(LocalTime startTime, Long dailyPatientCap) {
        log.trace("Calculating end time for appointments.");
        final int appointmentDurationMinutes = 15;
        long totalMinutes = dailyPatientCap * appointmentDurationMinutes;

        // Ensure that the end time does not exceed the end of the day.
        LocalTime endOfDay = LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES);
        LocalTime proposedEndTime = startTime.plusMinutes(totalMinutes);

        if (proposedEndTime.isBefore(startTime) || proposedEndTime.equals(LocalTime.MIDNIGHT) || proposedEndTime.isAfter(LocalTime.of(23, 59))) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "The calculated end time exceeds allowable hours. Please reduce the number of patients or adjust the start time.");
        }

        return proposedEndTime;
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
