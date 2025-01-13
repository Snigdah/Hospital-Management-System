package com.example.inventory.service.impl;

import com.example.inventory.dto.request.MedicalEquipmentCreateDTO;
import com.example.inventory.dto.request.MedicalEquipmentUpdateDTO;
import com.example.inventory.dto.request.MedicineUpdateDTO;
import com.example.inventory.dto.response.MedicalEquipmentResponseDTO;
import com.example.inventory.entity.MedicalEquipment;
import com.example.inventory.entity.Medicine;
import com.example.inventory.exception.CustomeException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.MedicalEquipmentRepository;
import com.example.inventory.service.IMedicalEquipmentService;
import com.example.inventory.utils.EnumValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MedicalEquipmentService implements IMedicalEquipmentService {
    private final MedicalEquipmentRepository medicalEquipmentRepository;
    public MedicalEquipmentService(MedicalEquipmentRepository medicalEquipmentRepository) {
        this.medicalEquipmentRepository = medicalEquipmentRepository;
    }

    // Create MedicalEquipment
    public void createMedicalEquipment(MedicalEquipmentCreateDTO request) {
        log.info("Creating new medical equipment: {}", request);

        // Validate lastMaintenanceDate
        validateLastMaintenanceDate(request.getLastMaintenanceDate());

        // Convert DTO to Entity and Save DB
        MedicalEquipment medicalEquipment = convertToEntity(request);
        medicalEquipmentRepository.save(medicalEquipment);
    }

    // Update Medical Equipment
    public void updateMedicalEquipment(MedicalEquipmentUpdateDTO equipmentUpdateDTO) {
        log.info("Updating medical equipment entry with ID: {}", equipmentUpdateDTO.getId());

        // Validate lastMaintenanceDate
        validateLastMaintenanceDate(equipmentUpdateDTO.getLastMaintenanceDate());

        // Fetch equipment and validate
        MedicalEquipment medicalEquipment = medicalEquipmentRepository.findById(equipmentUpdateDTO.getId())
                .orElseThrow(() -> {
                    log.error("Medical Equipment not found with ID: {}", equipmentUpdateDTO.getId());
                    return new ResourceNotFoundException("MedicalEquipment", "ID", equipmentUpdateDTO.getId());
                });

        // Map DTO to entity and save in DB
        mapDtoToEntity(equipmentUpdateDTO, medicalEquipment);
        medicalEquipmentRepository.save(medicalEquipment);
        log.info("Medical Equipment entry updated with ID: {}", medicalEquipment.getId());
    }

    // Get Medical Equipment by ID
    public MedicalEquipmentResponseDTO getMedicalEquipmentById(long id) {
        log.info("Fetching medical equipment with ID: {}", id);

        MedicalEquipment medicalEquipment = medicalEquipmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Medical Equipment not found with ID: {}", id);
                    return new ResourceNotFoundException("MedicalEquipment", "ID", id);
                });

        return convertEntityToDto(medicalEquipment);
    }

    // Fetch All Medical Equipments
    public List<MedicalEquipmentResponseDTO> getAllMedicalEquipments() {
        log.info("Fetching all medical equipments");

        List<MedicalEquipment> medicalEquipments = medicalEquipmentRepository.findAll();
        return medicalEquipments.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    // Delete Medical Equipment by ID
    public void deleteMedicalEquipmentById(long id) {
        log.info("Attempting to delete medical equipment with ID: {}", id);

        if (!medicalEquipmentRepository.existsById(id)) {
            log.error("Cannot delete medical equipment with ID: {} as it does not exist", id);
            throw new ResourceNotFoundException("MedicalEquipment", "ID", id);
        }

        medicalEquipmentRepository.deleteById(id);
        log.info("Medical equipment with ID: {} has been successfully deleted", id);
    }

    // Map DTO to Entity
    private void mapDtoToEntity(MedicalEquipmentUpdateDTO dto, MedicalEquipment entity) {
        entity.setEquipmentName(dto.getEquipmentName());
        entity.setManufacturerName(dto.getManufacturerName());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setQuantity(dto.getQuantity());
        entity.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        entity.setCategory(EnumValidation.parseEquipmentCategory(dto.getCategory()));
    }

    // Convert Entity to DTO
    private MedicalEquipmentResponseDTO convertEntityToDto(MedicalEquipment medicalEquipment) {
        MedicalEquipmentResponseDTO dto = new MedicalEquipmentResponseDTO();

        dto.setId(medicalEquipment.getId());
        dto.setEquipmentName(medicalEquipment.getEquipmentName());
        dto.setManufacturerName(medicalEquipment.getManufacturerName());
        dto.setSerialNumber(medicalEquipment.getSerialNumber());
        dto.setQuantity(medicalEquipment.getQuantity());
        dto.setLastMaintenanceDate(medicalEquipment.getLastMaintenanceDate());
        dto.setCategory(medicalEquipment.getCategory());

        return dto;
    }

    // Convert DTO to Entity
    private MedicalEquipment convertToEntity(MedicalEquipmentCreateDTO request) {
        return MedicalEquipment.builder()
                .equipmentName(request.getEquipmentName())
                .manufacturerName(request.getManufacturerName())
                .serialNumber(request.getSerialNumber())
                .quantity(request.getQuantity())
                .lastMaintenanceDate(request.getLastMaintenanceDate())
                .category(EnumValidation.parseEquipmentCategory(request.getCategory()))
                .build();
    }

    // Date Validation
    public void validateLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        LocalDate today = LocalDate.now();

        // Check if the last maintenance date is in the past
        if (lastMaintenanceDate != null && !lastMaintenanceDate.isBefore(today)) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Last maintenance date must be in the past.");
        }
    }
}
