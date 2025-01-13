package com.example.doctor.services;

import com.example.doctor.dto.request.AppointmentCreationDTO;
import com.example.doctor.dto.response.AppointmentDoctorDTO;
import com.example.doctor.dto.response.AppointmentResponseDTO;
import com.example.doctor.dto.response.AvailableSlotResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService {
    public void createAppointment(AppointmentCreationDTO appointmentCreationDTO, long patientId);
    public List<AppointmentResponseDTO> getAppointmentsByDoctorAndDate(long doctorId, LocalDate date);
    public List<AvailableSlotResponseDTO> getAvailableSlots(Long doctorId, LocalDate date);
    public List<AppointmentDoctorDTO> getPatientUpcomingAppointments(long patientId);
}
