package com.example.community.controller;

import com.example.community.dto.request.DoctorReviewCreateDTO;
import com.example.community.dto.response.DoctorReviewDTO;
import com.example.community.response.ResponseHandler;
import com.example.community.service.IAuthenticationService;
import com.example.community.service.IDoctorReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/community/doctor")
public class DoctorReviewController {
    private final IDoctorReviewService doctorReviewService;
    private final IAuthenticationService authenticationService;

    public DoctorReviewController(IDoctorReviewService doctorReviewService, IAuthenticationService authenticationService) {
        this.doctorReviewService = doctorReviewService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/review/create")
    public ResponseEntity<Object> createHospitalReview(@Valid @RequestBody DoctorReviewCreateDTO createDTO) {
        long userId = authenticationService.getAuthenticatedUser();
      doctorReviewService.createDoctorReview(createDTO, userId);
        return ResponseHandler.generateResponse("Create review successfully", HttpStatus.CREATED);
    }

    @GetMapping("review/{doctorId}")
    public ResponseEntity<Object> getAllReviewForSpecific(@PathVariable long doctorId){
        List<DoctorReviewDTO> response = doctorReviewService.getAllReviewForSpecificDoctor(doctorId);
        return ResponseHandler.generateResponse("Fetch All Review successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/review/delete/{doctorId}")
    public ResponseEntity<Object> deleteReviewForSpecificDoctor(@PathVariable long doctorId){
        long userId = authenticationService.getAuthenticatedUser();
        doctorReviewService.deleteDoctorReview(userId, doctorId);
        return ResponseHandler.generateResponse("Delete review successfully", HttpStatus.OK);
    }
}
