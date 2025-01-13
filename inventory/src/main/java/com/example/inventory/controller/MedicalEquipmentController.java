package com.example.inventory.controller;

import com.example.inventory.dto.request.MedicalEquipmentCreateDTO;
import com.example.inventory.dto.request.MedicalEquipmentUpdateDTO;
import com.example.inventory.response.ResponseHandler;
import com.example.inventory.service.IMedicalEquipmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory/equipment")
public class MedicalEquipmentController {
    private final IMedicalEquipmentService medicalEquipmentService;

    public MedicalEquipmentController(IMedicalEquipmentService medicalEquipmentService) {
        this.medicalEquipmentService = medicalEquipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createMedicalEquipment(@Valid @RequestBody MedicalEquipmentCreateDTO medicalEquipmentCreateDTO) {
        medicalEquipmentService.createMedicalEquipment(medicalEquipmentCreateDTO);
        return ResponseHandler.generateResponse("Medical Equipment created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMedicalEquipment(@Valid @RequestBody MedicalEquipmentUpdateDTO medicalEquipmentUpdateDTO) {
        medicalEquipmentService.updateMedicalEquipment(medicalEquipmentUpdateDTO);
        return ResponseHandler.generateResponse("Medical Equipment updated successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMedicalEquipmentById(@PathVariable long id) {
        return ResponseHandler.generateResponse("Medical Equipment fetched successfully", HttpStatus.OK, medicalEquipmentService.getMedicalEquipmentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllMedicalEquipments() {
        return ResponseHandler.generateResponse("All Medical Equipments fetched successfully", HttpStatus.OK, medicalEquipmentService.getAllMedicalEquipments());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMedicalEquipmentById(@PathVariable long id) {
        medicalEquipmentService.deleteMedicalEquipmentById(id);
        return ResponseHandler.generateResponse("Medical Equipment deleted successfully", HttpStatus.OK);
    }
}
