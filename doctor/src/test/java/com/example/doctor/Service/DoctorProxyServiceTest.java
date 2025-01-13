package com.example.doctor.Service;
import com.example.doctor.dto.response.DoctorCountDTO;
import com.example.doctor.model.Doctor;
import com.example.doctor.repository.DoctorRepository;
import com.example.doctor.services.impl.DoctorProxyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DoctorProxyServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorProxyService doctorProxyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsDoctorExistsOrNot_Exists() {
        long userId = 1L;
        when(doctorRepository.findByUserId(userId)).thenReturn(Optional.of(new Doctor()));

        boolean exists = doctorProxyService.isDoctorExistsOrNot(userId);

        assertTrue(exists);
        verify(doctorRepository).findByUserId(userId);
    }

    @Test
    public void testIsDoctorExistsOrNot_NotExists() {
        long userId = 2L;
        when(doctorRepository.findByUserId(userId)).thenReturn(Optional.empty());

        boolean exists = doctorProxyService.isDoctorExistsOrNot(userId);

        assertFalse(exists);
        verify(doctorRepository).findByUserId(userId);
    }

    @Test
    public void testTotalDoctorCount_NonZero() {
        long expectedCount = 5L;
        when(doctorRepository.count()).thenReturn(expectedCount);

        DoctorCountDTO result = doctorProxyService.totalDoctorCount();

        assertNotNull(result);
        assertEquals(expectedCount, result.getTotalDoctor());
        verify(doctorRepository).count();
    }

    @Test
    public void testTotalDoctorCount_Zero() {
        long expectedCount = 0L;
        when(doctorRepository.count()).thenReturn(expectedCount);

        DoctorCountDTO result = doctorProxyService.totalDoctorCount();

        assertNotNull(result);
        assertEquals(expectedCount, result.getTotalDoctor());
        verify(doctorRepository).count();
    }

}

