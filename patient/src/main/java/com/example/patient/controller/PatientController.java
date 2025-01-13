package com.example.patient.controller;

import com.example.patient.dto.response.HealthProfileDTO;
import com.example.patient.dto.response.PatientProfileDTO;
import com.example.patient.model.Patient;
import com.example.patient.response.ResponseHandler;
import com.example.patient.services.IAuthenticationService;
import com.example.patient.services.IPatientService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient/profile")
public class PatientController {

    private final IAuthenticationService authenticationService;
    private final IPatientService patientService;

    public PatientController(IAuthenticationService authenticationService, IPatientService patientService) {
        this.authenticationService = authenticationService;
        this.patientService = patientService;
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<Object> getUserInformation(@PathVariable long userId){
        long loginUserId = authenticationService.getAuthenticatedUser();
        PatientProfileDTO response = patientService.getPatientProfileByUserId(userId, loginUserId);
        return ResponseHandler.generateResponse("Fetch Patient Information Successfully", HttpStatus.OK, response);
    }

    @GetMapping("getAll")
    public ResponseEntity<Object> getAllPatientsProfileInfo(){
        List<PatientProfileDTO> response = patientService.getAllUserInformation();
        return ResponseHandler.generateResponse("Fetch all patient information", HttpStatus.OK, response);
    }

    @GetMapping("/health-info")
    public ResponseEntity<Object> getUserHealthInformation(){
        long userId = authenticationService.getAuthenticatedUser();
        HealthProfileDTO response = patientService.getHealthInfoByUserId(userId);
        return ResponseHandler.generateResponse("Fetch user health information successfully", HttpStatus.OK, response);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchPatient(@RequestParam(required = false) String bloodGroup,
                                       @RequestParam(defaultValue = "id") String sortBy,
                                       @RequestParam(defaultValue = "asc") String sortDir) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        List<Patient> response = patientService.searchPatients(bloodGroup, sortBy, direction);
        return ResponseHandler.generateResponse("Fetch Successfully", HttpStatus.OK, response);
    }
}
