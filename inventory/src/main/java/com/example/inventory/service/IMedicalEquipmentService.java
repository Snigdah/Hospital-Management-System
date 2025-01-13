package com.example.inventory.service;

import com.example.inventory.dto.request.MedicalEquipmentCreateDTO;
import com.example.inventory.dto.request.MedicalEquipmentUpdateDTO;
import com.example.inventory.dto.response.MedicalEquipmentResponseDTO;

import java.util.List;

public interface IMedicalEquipmentService {
    void createMedicalEquipment(MedicalEquipmentCreateDTO request);
    void updateMedicalEquipment(MedicalEquipmentUpdateDTO equipmentUpdateDTO);
    MedicalEquipmentResponseDTO getMedicalEquipmentById(long id);
    List<MedicalEquipmentResponseDTO> getAllMedicalEquipments();
    void deleteMedicalEquipmentById(long id);
}
