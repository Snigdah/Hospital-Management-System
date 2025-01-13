package com.example.inventory.controller;

import com.example.inventory.dto.request.MedicineCreateDTO;
import com.example.inventory.dto.request.MedicineUpdateDTO;
import com.example.inventory.response.ResponseHandler;
import com.example.inventory.service.IMedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory/medicine")
public class MedicineController {
    private final IMedicineService medicineService;

    public MedicineController(IMedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createMedicine(@Valid @RequestBody MedicineCreateDTO medicineCreateDTO){
        medicineService.createMedicine(medicineCreateDTO);
        return ResponseHandler.generateResponse("Create Medicine Successfully", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMedicine(@Valid @RequestBody MedicineUpdateDTO medicineUpdateDTO) {
        medicineService.updateMedicine(medicineUpdateDTO);
        return ResponseHandler.generateResponse("Medicine updated successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMedicineById(@PathVariable long id) {
        return ResponseHandler.generateResponse("Medicine fetched successfully", HttpStatus.OK, medicineService.getMedicineById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllMedicines() {
        return ResponseHandler.generateResponse("All medicines fetched successfully", HttpStatus.OK, medicineService.getAllMedicines());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMedicineById(@PathVariable long id) {
        medicineService.deleteMedicineById(id);
        return ResponseHandler.generateResponse("Medicine deleted successfully", HttpStatus.OK);
    }


}
