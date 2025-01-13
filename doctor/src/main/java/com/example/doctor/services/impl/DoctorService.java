package com.example.doctor.services.impl;

import com.example.doctor.dto.response.DoctorDTO;
import com.example.doctor.dto.response.DoctorInfoDTO;
import com.example.doctor.dto.response.RoomInfoDTO;
import com.example.doctor.exception.AuthenticationException;
import com.example.doctor.exception.CustomeException;
import com.example.doctor.exception.FeignCustomException;
import com.example.doctor.exception.ResourceNotFoundException;
import com.example.doctor.feingClient.AdminServiceClient;
import com.example.doctor.model.Doctor;
import com.example.doctor.repository.DoctorRepository;
import com.example.doctor.services.IDoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DoctorService implements IDoctorService {
    private final DoctorRepository doctorRepository;
    private final AdminServiceClient adminServiceClient;

    public DoctorService(DoctorRepository doctorRepository, AdminServiceClient adminServiceClient) {
        this.doctorRepository = doctorRepository;
        this.adminServiceClient = adminServiceClient;
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        // Retrieve the list of all Doctor entities from the database
        List<Doctor> doctors = doctorRepository.findAll();

        // Map each Doctor entity to a DoctorDTO
        return doctors.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDTO> getAllDoctorsByDeptId(long deptId) {
        log.info("Fetching all doctors for department ID: {}", deptId);
        List<Doctor> doctors = doctorRepository.findByDeptId(deptId);

        // validate the list
        if (doctors.isEmpty()) {
            log.error("No Doctor found with this ID : {}", deptId);
            throw new CustomeException(HttpStatus.BAD_REQUEST, "NO Doctor Found in this specific Department");
        }

        return doctors.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public DoctorInfoDTO getDoctorById(long doctorId) {
        log.info("Fetching doctor with ID: {}", doctorId);

        // Fetch Doctor & Room information and validate
        Doctor doctor = findDoctorById(doctorId);
        RoomInfoDTO roomInfo = fetchRoomInfoForDoctor(doctor.getRoomId());

        // Convert and return the combined Doctor information
        return convertToDoctorDTO(doctor, roomInfo);
    }

    // Fetches doctor by ID or throws an exception if not found
    public Doctor findDoctorById(long doctorId) {
        return doctorRepository.findByUserId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));
    }

    // Fetch room information form Admin Microservice
    public RoomInfoDTO fetchRoomInfoForDoctor(long roomId) {
        try {
            log.debug("Retrieving room information for room ID: {}", roomId);
            return adminServiceClient.getRoomInformation(roomId);
        } catch (FeignCustomException ex) {
            log.error("An error occurred while fetching room information for room ID: {}", roomId, ex);
            throw new AuthenticationException(ex.getStatusCode(), ex.getErrorDetails().getMessage());
        }
    }

    // Change Status
    @Override
    public void changeStatus(long userId) {
        log.info("Attempting to change status");
        // Fetch doctor and validate
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("No Doctor found with this ID : {}", userId);
                    return new ResourceNotFoundException("Doctor", "id", userId);
                });

        // Change Status and save in DB
        doctor.setActive(!doctor.isActive());
        doctorRepository.save(doctor);
    }

    // Helper method to map a Doctor entity to a DoctorDTO
    private DoctorInfoDTO convertToDoctorDTO(Doctor doctor, RoomInfoDTO roomInfo) {
        log.debug("Mapping Doctor entity to DoctorDTO: {}", doctor.getId());

        // Here, map all fields from the Doctor entity to the DoctorDTO.
        return DoctorInfoDTO.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .deptName(roomInfo.getDeptName())
                .roomNumber(roomInfo.getRoomNumber())
                .roomType(roomInfo.getRoomType())
                .doctorName(doctor.getDoctorName())
                .imgUrl(doctor.getImgUrl())
                .yearOfExp(doctor.getYearOfExp())
                .startTime(doctor.getStartTime())
                .endTime(doctor.getEndTime())
                .dailyPatientCap(doctor.getDailyPatientCap())
                .phone(doctor.getPhone())
                .medicalName(doctor.getMedicalName())
                .active(doctor.isActive())
                .medicalDegree(doctor.getMedicalDegree().toString())
                .medicalDesignation(doctor.getMedicalDesignation().toString())
                .department(doctor.getDepartment())
                .build();
    }

    // Helper method to map a Doctor entity to a DoctorDTO
    private DoctorDTO mapToDTO(Doctor doctor) {
        log.debug("Mapping Doctor entity to DoctorDTO: {}", doctor.getId());
        // Here, map all fields from the Doctor entity to the DoctorDTO.
        return DoctorDTO.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .deptId(doctor.getDeptId())
                .roomId(doctor.getRoomId())
                .doctorName(doctor.getDoctorName())
                .imgUrl(doctor.getImgUrl())
                .yearOfExp(doctor.getYearOfExp())
                .startTime(doctor.getStartTime())
                .endTime(doctor.getEndTime())
                .dailyPatientCap(doctor.getDailyPatientCap())
                .phone(doctor.getPhone())
                .medicalName(doctor.getMedicalName())
                .active(doctor.isActive())
                .medicalDegree(doctor.getMedicalDegree().toString())
                .medicalDesignation(doctor.getMedicalDesignation().toString())
                .department(doctor.getDepartment())
                .build();
    }

    // Search Doctor
    @Override
    public List<Doctor> searchDoctors(String department,
                                      String medicalDesignation,
                                      String medicalDegree,
                                      Long yearOfExp,
                                      String sortBy,
                                      Sort.Direction sortDir) {
        Sort sort = Sort.by(sortDir, sortBy);
        return doctorRepository.findAll(DoctorSpecification.withDynamicQuery(department, medicalDesignation, medicalDegree, yearOfExp), sort);
    }
}
