package com.example.doctor.services.impl;
import com.example.doctor.dto.response.DashboardResponseDTO;
import com.example.doctor.model.Appointment;
import com.example.doctor.model.enums.AppointmentType;
import com.example.doctor.repository.AppointmentRepository;
import com.example.doctor.services.IDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class DashboardService implements IDashboardService {
    private final AppointmentRepository appointmentRepository;
    public DashboardService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Dashboard Response
    public DashboardResponseDTO getDashboardCount(long doctorId) {
        DashboardResponseDTO dto = new DashboardResponseDTO();
        dto.setTotalPatient(getTotalUniquePatientsForDoctor(doctorId));
        dto.setTotalAppointment(getTotalAppointmentsForDoctor(doctorId));
        dto.setTotalOnlineAppointments(getTotalTelemedicineAppointmentsForDoctor(doctorId));
        dto.setTodayAppointment(getTotalAppointmentsForDoctorToday(doctorId));
        return dto;
    }

    // Get totally patient for a Doctor
    public int getTotalUniquePatientsForDoctor(long doctorId) {
        List<Long> patientIds = appointmentRepository.findDistinctPatientIdsByDoctorId(doctorId);
        return patientIds.size(); // This gives the count of unique patients
    }

    // Get totally appeasements for a doctors lifetime
    public int getTotalAppointmentsForDoctor(long doctorId) {
        return appointmentRepository.countByDoctorId(doctorId);
    }

    // Totally appointments for a doctor today
    public int getTotalAppointmentsForDoctorToday(long doctorId) {
        LocalDate today = LocalDate.now();
        List<Appointment> result = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, today);
        return result.size();
    }

    // Totally Number of Telemedicine Appointment for a doctor
    public int getTotalTelemedicineAppointmentsForDoctor(long doctorId) {
        return appointmentRepository.countByDoctorIdAndAppointmentType(doctorId, AppointmentType.TELEMEDICINE);
    }
}
