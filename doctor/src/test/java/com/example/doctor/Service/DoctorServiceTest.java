package com.example.doctor.Service;

import com.example.doctor.dto.response.DoctorInfoDTO;
import com.example.doctor.dto.response.RoomInfoDTO;
import com.example.doctor.exception.AuthenticationException;
import com.example.doctor.exception.FeignCustomException;
import com.example.doctor.exception.ResourceNotFoundException;
import com.example.doctor.feingClient.AdminServiceClient;
import com.example.doctor.model.Doctor;
import com.example.doctor.model.enums.MedicalDegree;
import com.example.doctor.model.enums.MedicalDesignation;
import com.example.doctor.repository.DoctorRepository;
import com.example.doctor.services.impl.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AdminServiceClient adminServiceClient;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindDoctorByIdExists() {
        long doctorId = 1L;
        Doctor expectedDoctor = Doctor.builder()
                .id(doctorId)
                .userId(123L)
                .deptId(456L)
                .roomId(789L)
                .doctorName("Dr. Alice")
                .imgUrl("img_url")
                .yearOfExp(15)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(16, 0))
                .dailyPatientCap(20)
                .phone("111-222-3333")
                .medicalName("General Practice")
                .active(true)
                .medicalDegree(MedicalDegree.MD)
                .medicalDesignation(MedicalDesignation.JUNIOR_DOCTOR)
                .department("General Medicine")
                .build();

        when(doctorRepository.findByUserId(doctorId)).thenReturn(Optional.of(expectedDoctor));

        Doctor actualDoctor = doctorService.findDoctorById(doctorId);

        assertNotNull(actualDoctor);
        assertEquals(expectedDoctor.getId(), actualDoctor.getId());
        assertEquals(expectedDoctor.getUserId(), actualDoctor.getUserId());
        assertEquals(expectedDoctor.getDeptId(), actualDoctor.getDeptId());
        assertEquals(expectedDoctor.getRoomId(), actualDoctor.getRoomId());
        assertEquals(expectedDoctor.getDoctorName(), actualDoctor.getDoctorName());
        assertEquals(expectedDoctor.getImgUrl(), actualDoctor.getImgUrl());
        assertEquals(expectedDoctor.getYearOfExp(), actualDoctor.getYearOfExp());
        assertEquals(expectedDoctor.getStartTime(), actualDoctor.getStartTime());
        assertEquals(expectedDoctor.getEndTime(), actualDoctor.getEndTime());
        assertEquals(expectedDoctor.getDailyPatientCap(), actualDoctor.getDailyPatientCap());
        assertEquals(expectedDoctor.getPhone(), actualDoctor.getPhone());
        assertEquals(expectedDoctor.getMedicalName(), actualDoctor.getMedicalName());
        assertEquals(expectedDoctor.isActive(), actualDoctor.isActive());
        assertEquals(expectedDoctor.getMedicalDegree(), actualDoctor.getMedicalDegree());
        assertEquals(expectedDoctor.getMedicalDesignation(), actualDoctor.getMedicalDesignation());
        assertEquals(expectedDoctor.getDepartment(), actualDoctor.getDepartment());

        verify(doctorRepository).findByUserId(doctorId);
    }

    @Test
    public void testFindDoctorByIdNotExists() {
        long doctorId = 2L;

        when(doctorRepository.findByUserId(doctorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.findDoctorById(doctorId);
        });

        verify(doctorRepository).findByUserId(doctorId);
    }

    @Test
    public void testFetchRoomInfoForDoctorSuccess() {
        long roomId = 101L;
        RoomInfoDTO expectedRoomInfo = RoomInfoDTO.builder()
                .roomNumber(roomId)
                .deptName("Cardiology")
                .roomType("General")
                .build();

        when(adminServiceClient.getRoomInformation(roomId)).thenReturn(expectedRoomInfo);

        RoomInfoDTO actualRoomInfo = doctorService.fetchRoomInfoForDoctor(roomId);

        assertNotNull(actualRoomInfo);
        assertEquals(expectedRoomInfo.getRoomNumber(), actualRoomInfo.getRoomNumber());
        assertEquals(expectedRoomInfo.getDeptName(), actualRoomInfo.getDeptName());
        assertEquals(expectedRoomInfo.getRoomType(), actualRoomInfo.getRoomType());

        verify(adminServiceClient).getRoomInformation(roomId);
    }

    @Test
    public void testChangeStatusSuccess() {
        long userId = 1L;
        Doctor mockDoctor = Doctor.builder()
                .userId(userId)
                .active(false)
                .build();

        when(doctorRepository.findByUserId(userId)).thenReturn(Optional.of(mockDoctor));

        doctorService.changeStatus(userId);

        assertTrue(mockDoctor.isActive());
        verify(doctorRepository).findByUserId(userId);
        verify(doctorRepository).save(mockDoctor);
    }

    @Test
    public void testChangeStatusDoctorNotFound() {
        long userId = 2L;

        when(doctorRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.changeStatus(userId);
        });

        verify(doctorRepository).findByUserId(userId);
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    public void testChangeStatusFromActiveToInactive() {
        long userId = 3L;
        Doctor mockDoctor = Doctor.builder()
                .userId(userId)
                .active(true) // Initially active
                .build();

        when(doctorRepository.findByUserId(userId)).thenReturn(Optional.of(mockDoctor));

        doctorService.changeStatus(userId);

        assertFalse(mockDoctor.isActive()); // Should now be inactive
        verify(doctorRepository).findByUserId(userId);
        verify(doctorRepository).save(mockDoctor);
    }
    @Test
    public void testToggleStatusBackAndForth() {
        long userId = 4L;
        Doctor mockDoctor = Doctor.builder()
                .userId(userId)
                .active(false) // Initially inactive
                .build();

        when(doctorRepository.findByUserId(userId)).thenReturn(Optional.of(mockDoctor));

        // First toggle: Inactive to Active
        doctorService.changeStatus(userId);
        assertTrue(mockDoctor.isActive());

        // Second toggle: Active to Inactive
        doctorService.changeStatus(userId);
        assertFalse(mockDoctor.isActive());

        verify(doctorRepository, times(2)).findByUserId(userId);
        verify(doctorRepository, times(2)).save(mockDoctor);
    }

}
