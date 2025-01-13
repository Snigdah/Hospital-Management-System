package com.example.doctor.controller;

import com.example.doctor.dto.request.AppointmentCreationDTO;
import com.example.doctor.dto.response.AppointmentDoctorDTO;
import com.example.doctor.dto.response.AppointmentResponseDTO;
import com.example.doctor.dto.response.AvailableSlotResponseDTO;
import com.example.doctor.response.ResponseHandler;
import com.example.doctor.services.IAppointmentService;
import com.example.doctor.services.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor/appointment")
public class AppointmentController {

    private final IAuthenticationService authenticationService;
    private final IAppointmentService appointmentService;

    public AppointmentController(IAuthenticationService authenticationService, IAppointmentService appointmentService) {
        this.authenticationService = authenticationService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody AppointmentCreationDTO appointmentCreationDTO) {
        long userId = authenticationService.getAuthenticatedUser();
        appointmentService.createAppointment(appointmentCreationDTO, userId);
        return ResponseHandler.generateResponse("Appointment created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllAppointments() {
        long userId = authenticationService.getAuthenticatedUser();
        List<AppointmentResponseDTO> response = appointmentService.getAppointmentsByDoctorAndDate(userId, LocalDate.now());
        return ResponseHandler.generateResponse("Fetch all appointment successfully", HttpStatus.OK, response);
    }

    @GetMapping("/get/availableSlots/doctor/{doctorId}")
    public ResponseEntity<Object> getAllAppointments(@PathVariable Long doctorId,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AvailableSlotResponseDTO> response = appointmentService.getAvailableSlots(doctorId, date);
        return ResponseHandler.generateResponse("Fetch all appointment successfully", HttpStatus.OK, response);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Object> getUpcomingAppointments(){
        long userId = authenticationService.getAuthenticatedUser();
        List<AppointmentDoctorDTO> response = appointmentService.getPatientUpcomingAppointments(userId);
        return ResponseHandler.generateResponse("Fetch data successfully", HttpStatus.OK, response);
    }
}
