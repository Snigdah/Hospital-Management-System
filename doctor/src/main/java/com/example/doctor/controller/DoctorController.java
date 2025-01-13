package com.example.doctor.controller;

import com.example.doctor.dto.response.DoctorDTO;
import com.example.doctor.dto.response.DoctorInfoDTO;
import com.example.doctor.model.Doctor;
import com.example.doctor.response.ResponseHandler;
import com.example.doctor.services.IAuthenticationService;
import com.example.doctor.services.IDoctorService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor/info")
public class DoctorController {
    private final IDoctorService doctorService;
    private final IAuthenticationService authenticationService;

    public DoctorController(IDoctorService doctorService, IAuthenticationService authenticationService) {
        this.doctorService = doctorService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllDoctors(){
        List<DoctorDTO> response = doctorService.getAllDoctors();
        return ResponseHandler.generateResponse("Fetch All Data Successfully", HttpStatus.OK,response);
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<Object> getAllDoctorsByDeptId(@PathVariable long deptId){
        List<DoctorDTO> response = doctorService.getAllDoctorsByDeptId(deptId);
        return ResponseHandler.generateResponse("Fetch All Data Successfully", HttpStatus.OK,response);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<Object> getDoctorById(@PathVariable long  doctorId){
        DoctorInfoDTO response = doctorService.getDoctorById(doctorId);
        return ResponseHandler.generateResponse("Fetch All Data Successfully", HttpStatus.OK,response);
    }

    @GetMapping("/search")
    public ResponseEntity<Object>searchDoctors(@RequestParam(required = false) String department,
                                      @RequestParam(required = false) String medicalDesignation,
                                      @RequestParam(required = false) String medicalDegree,
                                       @RequestParam(required = false) Long yearOfExp,
                                       @RequestParam(defaultValue = "id") String sortBy,
                                       @RequestParam(defaultValue = "asc") String sortDir) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        List<Doctor>  response  = doctorService.searchDoctors(department, medicalDesignation, medicalDegree, yearOfExp, sortBy, direction);
        return ResponseHandler.generateResponse("Fetch Successfully", HttpStatus.OK, response);
    }

    @GetMapping("/changeStatus")
    public ResponseEntity<Object> changeStatus(){
        long userId = authenticationService.getAuthenticatedUser();
        doctorService.changeStatus(userId);
        return ResponseHandler.generateResponse("Change Status successfully", HttpStatus.OK);
    }

}
