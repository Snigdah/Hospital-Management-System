package com.example.inventory.service;

import com.example.inventory.dto.request.MedicineCreateDTO;
import com.example.inventory.dto.request.MedicineUpdateDTO;
import com.example.inventory.dto.response.MedicineResponseDTO;

import java.util.List;

public interface IMedicineService {
    void createMedicine(MedicineCreateDTO medicineCreateDTO);
    void updateMedicine(MedicineUpdateDTO medicineUpdateDTO);
    MedicineResponseDTO getMedicineById(long id);
    List<MedicineResponseDTO> getAllMedicines();
    void deleteMedicineById(long id);
}
