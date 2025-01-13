package com.example.doctor.Service;
import com.example.doctor.model.Appointment;
import com.example.doctor.model.enums.AppointmentType;
import com.example.doctor.repository.AppointmentRepository;
import com.example.doctor.services.impl.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DashboardServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTotalUniquePatientsForDoctor_HasPatients() {
        long doctorId = 1L;
        List<Long> mockPatientIds = Arrays.asList(1L, 2L, 3L); // Assuming unique patient IDs
        when(appointmentRepository.findDistinctPatientIdsByDoctorId(doctorId)).thenReturn(mockPatientIds);

        int patientCount = dashboardService.getTotalUniquePatientsForDoctor(doctorId);

        assertEquals(3, patientCount); // Expecting 3 unique patients
        verify(appointmentRepository).findDistinctPatientIdsByDoctorId(doctorId);
    }

    @Test
    public void testGetTotalUniquePatientsForDoctor_NoPatients() {
        long doctorId = 2L;
        List<Long> mockPatientIds = Collections.emptyList(); // No patients
        when(appointmentRepository.findDistinctPatientIdsByDoctorId(doctorId)).thenReturn(mockPatientIds);

        int patientCount = dashboardService.getTotalUniquePatientsForDoctor(doctorId);

        assertEquals(0, patientCount); // Expecting 0 patients
        verify(appointmentRepository).findDistinctPatientIdsByDoctorId(doctorId);
    }

    @Test
    public void testGetTotalAppointmentsForDoctor_HasAppointments() {
        long doctorId = 1L;
        int expectedCount = 5;
        when(appointmentRepository.countByDoctorId(doctorId)).thenReturn(expectedCount);

        int actualCount = dashboardService.getTotalAppointmentsForDoctor(doctorId);

        assertEquals(expectedCount, actualCount);
        verify(appointmentRepository).countByDoctorId(doctorId);
    }

    @Test
    public void testGetTotalAppointmentsForDoctor_NoAppointments() {
        long doctorId = 2L;
        when(appointmentRepository.countByDoctorId(doctorId)).thenReturn(0);

        int actualCount = dashboardService.getTotalAppointmentsForDoctor(doctorId);

        assertEquals(0, actualCount);
        verify(appointmentRepository).countByDoctorId(doctorId);
    }

    @Test
    public void testGetTotalAppointmentsForDoctorToday_HasAppointments() {
        long doctorId = 1L;
        LocalDate today = LocalDate.now();
        List<Appointment> mockAppointments = Arrays.asList(new Appointment(), new Appointment()); // Assuming two appointments today

        when(appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, today)).thenReturn(mockAppointments);

        int actualCount = dashboardService.getTotalAppointmentsForDoctorToday(doctorId);

        assertEquals(2, actualCount); // Expecting 2 appointments
        verify(appointmentRepository).findByDoctorIdAndAppointmentDate(doctorId, today);
    }

    @Test
    public void testGetTotalAppointmentsForDoctorToday_NoAppointments() {
        long doctorId = 2L;
        LocalDate today = LocalDate.now();
        List<Appointment> mockAppointments = List.of(); // No appointments today

        when(appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, today)).thenReturn(mockAppointments);

        int actualCount = dashboardService.getTotalAppointmentsForDoctorToday(doctorId);

        assertEquals(0, actualCount); // Expecting 0 appointments
        verify(appointmentRepository).findByDoctorIdAndAppointmentDate(doctorId, today);
    }

    @Test
    public void testGetTotalTelemedicineAppointmentsForDoctor_HasAppointments() {
        long doctorId = 1L;
        int expectedCount = 3; // Assuming 3 telemedicine appointments
        when(appointmentRepository.countByDoctorIdAndAppointmentType(doctorId, AppointmentType.TELEMEDICINE)).thenReturn(expectedCount);

        int actualCount = dashboardService.getTotalTelemedicineAppointmentsForDoctor(doctorId);

        assertEquals(expectedCount, actualCount);
        verify(appointmentRepository).countByDoctorIdAndAppointmentType(doctorId, AppointmentType.TELEMEDICINE);
    }

    @Test
    public void testGetTotalTelemedicineAppointmentsForDoctor_NoAppointments() {
        long doctorId = 2L;
        when(appointmentRepository.countByDoctorIdAndAppointmentType(doctorId, AppointmentType.TELEMEDICINE)).thenReturn(0);

        int actualCount = dashboardService.getTotalTelemedicineAppointmentsForDoctor(doctorId);

        assertEquals(0, actualCount);
        verify(appointmentRepository).countByDoctorIdAndAppointmentType(doctorId, AppointmentType.TELEMEDICINE);
    }

}
