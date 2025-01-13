package com.example.inventory.service.impl;

import com.example.inventory.dto.request.MedicineCreateDTO;
import com.example.inventory.dto.request.MedicineUpdateDTO;
import com.example.inventory.dto.response.MedicineResponseDTO;
import com.example.inventory.entity.Medicine;
import com.example.inventory.exception.CustomeException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.MedicineRepository;
import com.example.inventory.service.IMedicineService;
import com.example.inventory.utils.EnumValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MedicineService implements IMedicineService {
    private final MedicineRepository medicineRepository;
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    // Create medicine
    public void createMedicine(MedicineCreateDTO medicineCreateDTO) {
        log.info("Creating a new medicine entry with name: {}", medicineCreateDTO.getMedicineName());

        // Validate Dates
        validateMedicineDates(medicineCreateDTO.getProductionDate(), medicineCreateDTO.getExpirationDate());

        // Map DTO to Entity
        Medicine medicine = buildMedicineEntity(medicineCreateDTO);

        // Save in DB
        Medicine savedMedicine = medicineRepository.save(medicine);
        log.info("Medicine entry created with ID: {}", savedMedicine.getId());
    }

    // Update Medicine
    public void updateMedicine(MedicineUpdateDTO medicineUpdateDTO) {
        log.info("Updating medicine entry with ID: {}", medicineUpdateDTO.getId());

        // Validate Dates
        validateMedicineDates(medicineUpdateDTO.getProductionDate(), medicineUpdateDTO.getExpirationDate());

        // Fetch medicine and Validate Medicine
        Medicine medicine = medicineRepository.findById(medicineUpdateDTO.getId())
                .orElseThrow(() -> {
                    log.error("Medicine not found with ID: {}", medicineUpdateDTO.getId());
                    return new ResourceNotFoundException("Medicine", "ID", medicineUpdateDTO.getId());
                });

        // Map DTO to entity and save in DB
        Medicine updatedMedicine = mapDtoToMedicineEntity(medicineUpdateDTO, medicine);
        medicineRepository.save(medicine);
        log.info("Medicine entry updated with ID: {}", medicine.getId());
    }

    // Get Medicine by medicine ID
    public MedicineResponseDTO getMedicineById(long id) {
        log.info("Fetching medicine with ID: {}", id);

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Medicine not found with ID: {}", id);
                    return new ResourceNotFoundException("Medicine", "ID", id);
                });

        return convertEntityToDto(medicine);
    }

    // Fetch All Medicine
    public List<MedicineResponseDTO> getAllMedicines() {
        log.info("Fetching all medicines");

        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    // Delete Medicine by ID
    public void deleteMedicineById(long id) {
        log.info("Attempting to delete medicine with ID: {}", id);

        if (!medicineRepository.existsById(id)) {
            log.error("Cannot delete medicine with ID: {} as it does not exist", id);
            throw new ResourceNotFoundException("Medicine", "ID", id);
        }

        medicineRepository.deleteById(id);
        log.info("Medicine with ID: {} has been successfully deleted", id);
    }

    // Valid Date
    public void validateMedicineDates(LocalDate productionDate, LocalDate expirationDate) {
        LocalDate today = LocalDate.now();

        if (productionDate != null && productionDate.isAfter(today)) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Production date cannot be in the future.");
        }

        if (expirationDate != null && expirationDate.isBefore(today)) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Expiration date must be in the future.");
        }
    }


    // Map DTO to Entity
    private Medicine mapDtoToMedicineEntity(MedicineUpdateDTO dto, Medicine medicine) {
        medicine.setMedicineName(dto.getMedicineName());
        medicine.setDosage(dto.getDosage());
        medicine.setManufacturerName(dto.getManufacturerName());
        medicine.setBatchNumber(dto.getBatchNumber());
        medicine.setQuantity(dto.getQuantity());
        medicine.setProductionDate(dto.getProductionDate());
        medicine.setExpirationDate(dto.getExpirationDate());
        medicine.setDescription(dto.getDescription());
        medicine.setCategoryName(EnumValidation.parseMedicineCategory(dto.getCategoryName()));
        medicine.setMedicineType(EnumValidation.parseMedicineType(dto.getMedicineType()));
        return medicine;
    }

    // Map DTO to Entity
    private Medicine buildMedicineEntity(MedicineCreateDTO dto) {
        return Medicine.builder()
                .medicineName(dto.getMedicineName())
                .dosage(dto.getDosage())
                .manufacturerName(dto.getManufacturerName())
                .batchNumber(dto.getBatchNumber())
                .quantity(dto.getQuantity())
                .productionDate(dto.getProductionDate())
                .expirationDate(dto.getExpirationDate())
                .description(dto.getDescription())
                .categoryName(EnumValidation.parseMedicineCategory(dto.getCategoryName()))
                .medicineType(EnumValidation.parseMedicineType(dto.getMedicineType()))
                .build();
    }

    // Map Entity to DTO
    public MedicineResponseDTO convertEntityToDto(Medicine medicine) {
        return MedicineResponseDTO.builder()
                .id(medicine.getId())
                .medicineName(medicine.getMedicineName())
                .dosage(medicine.getDosage())
                .manufacturerName(medicine.getManufacturerName())
                .batchNumber(medicine.getBatchNumber())
                .quantity(medicine.getQuantity())
                .productionDate(medicine.getProductionDate())
                .expirationDate(medicine.getExpirationDate())
                .description(medicine.getDescription())
                .categoryName(medicine.getCategoryName())
                .medicineType(medicine.getMedicineType())
                .build();
    }

}
