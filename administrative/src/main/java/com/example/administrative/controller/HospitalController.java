package com.example.administrative.controller;

import com.example.administrative.dto.request.DeleteDeptDto;
import com.example.administrative.dto.request.DepartmentDTO;
import com.example.administrative.dto.response.DepartmentDetailsDTO;
import com.example.administrative.response.ResponseHandler;
import com.example.administrative.services.IHospitalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/administrative/hospital")
public class HospitalController {

    private final IHospitalService hospitalService;

    @PostMapping("/createDept")
    public ResponseEntity<Object> createDeptWithRooms(@Valid @RequestBody DepartmentDTO departmentDTO){
        hospitalService.createDepartmentWithRooms(departmentDTO);
        return ResponseHandler.generateResponse("Create Dept with Rooms Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/getAllDept")
    public ResponseEntity<Object> getAllDeptDetails(){
        List<DepartmentDetailsDTO> response = hospitalService.getAllDepartmentsWithRoomDetails();
        return ResponseHandler.generateResponse("Fetch All Dept Successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/deleteDept")
    public ResponseEntity<Object> deleteDepartment(@Valid @RequestBody DeleteDeptDto department){
        hospitalService.deleteDepartment(department);
        return ResponseHandler.generateResponse("Delete Department Successfully", HttpStatus.OK);
    }
}
