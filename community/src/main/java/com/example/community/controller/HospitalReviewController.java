package com.example.community.controller;

import com.example.community.dto.request.HospitalReviewCreateDTO;
import com.example.community.dto.response.HospitalReviewDTO;
import com.example.community.response.ResponseHandler;
import com.example.community.service.IAuthenticationService;
import com.example.community.service.IHospitalReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/community/hospital")
public class HospitalReviewController {

    private final IHospitalReviewService hospitalReviewService;
    private final IAuthenticationService authenticationService;

    public HospitalReviewController(IHospitalReviewService hospitalReviewService, IAuthenticationService authenticationService) {
        this.hospitalReviewService = hospitalReviewService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createHospitalReview(@Valid @RequestBody HospitalReviewCreateDTO createDTO) {
        long userId = authenticationService.getAuthenticatedUser();
        hospitalReviewService.createHospitalReview(userId, createDTO);
        return ResponseHandler.generateResponse("Create review successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable long id){
        long userId = authenticationService.getAuthenticatedUser();
        hospitalReviewService.deleteReview(id, userId);
        return ResponseHandler.generateResponse("Delete review successfully", HttpStatus.OK);
    }

    //public List<HospitalReviewDTO> getAllReviewsWithUserName();
    @GetMapping("/reviews")
    public ResponseEntity<Object> getAllReviews(){
        List<HospitalReviewDTO> response = hospitalReviewService.getAllReviewsWithUserName();
        return ResponseHandler.generateResponse("Fetch all reviews successfully", HttpStatus.OK, response);
    }
}
