package com.example.inventory.service;

import com.example.inventory.dto.request.MedicineCreateDTO;
import com.example.inventory.dto.request.MedicineUpdateDTO;
import com.example.inventory.dto.response.MedicineResponseDTO;
import com.example.inventory.entity.Medicine;
import com.example.inventory.entity.enums.MedicineCategory;
import com.example.inventory.entity.enums.MedicineType;
import com.example.inventory.exception.CustomeException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.MedicineRepository;
import com.example.inventory.service.impl.MedicineService;
import com.example.inventory.utils.EnumValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;


public class MedicineServiceTest {
    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineService medicineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProductionDateInTheFuture() {
        LocalDate productionDate = LocalDate.now().plusDays(10); // Future date
        LocalDate expirationDate = LocalDate.now().plusDays(20); // Future expiration date

        CustomeException thrown = assertThrows(
                CustomeException.class,
                () -> medicineService.validateMedicineDates(productionDate, expirationDate),
                "Expected an exception for future production date"
        );

        assertTrue(thrown.getMessage().contains("Production date cannot be in the future."));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    public void testExpirationDateInThePast() {
        LocalDate productionDate = LocalDate.now().minusDays(10); // Past date
        LocalDate expirationDate = LocalDate.now().minusDays(1); // Past expiration date

        CustomeException thrown = assertThrows(
                CustomeException.class,
                () -> medicineService.validateMedicineDates(productionDate, expirationDate),
                "Expected an exception for past expiration date"
        );

        assertTrue(thrown.getMessage().contains("Expiration date must be in the future."));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    public void testBothDatesValid() {
        LocalDate productionDate = LocalDate.now().minusDays(10); // Past date
        LocalDate expirationDate = LocalDate.now().plusDays(10); // Future expiration date

        assertDoesNotThrow(
                () -> medicineService.validateMedicineDates(productionDate, expirationDate),
                "No exception expected for valid dates"
        );
    }

    @Test
    public void testNullProductionDate() {
        LocalDate expirationDate = LocalDate.now().plusDays(10); // Future expiration date

        assertDoesNotThrow(
                () -> medicineService.validateMedicineDates(null, expirationDate),
                "No exception expected for null production date"
        );
    }

    @Test
    public void testNullExpirationDate() {
        LocalDate productionDate = LocalDate.now().minusDays(10); // Past date

        assertDoesNotThrow(
                () -> medicineService.validateMedicineDates(productionDate, null),
                "No exception expected for null expiration date"
        );
    }

    @Test
    public void testBothDatesNull() {
        assertDoesNotThrow(
                () -> medicineService.validateMedicineDates(null, null),
                "No exception expected for both dates being null"
        );
    }

    @Test
    public void testDeleteMedicineByIdWhenMedicineExists() {
        long id = 1L;
        when(medicineRepository.existsById(id)).thenReturn(true);

        medicineService.deleteMedicineById(id);

        verify(medicineRepository).deleteById(id);
    }
    @Test
    public void testDeleteMedicineByIdWhenMedicineDoesNotExist() {
        long id = 1L;
        when(medicineRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            medicineService.deleteMedicineById(id);
        });

        verify(medicineRepository, never()).deleteById(id);
    }
    @Test
    public void testCreateMedicineSuccess() {
        MedicineCreateDTO dto = MedicineCreateDTO.builder()
                .medicineName("Aspirin")
                .dosage("500mg")
                .manufacturerName("Pharma Inc.")
                .batchNumber("B1234")
                .quantity(100L)
                .productionDate(LocalDate.now().minusDays(30))
                .expirationDate(LocalDate.now().plusDays(365))
                .description("Pain reliever")
                .categoryName("ANTIDEPRESSANTS")
                .medicineType("Tablet")
                .build();

        Medicine expectedMedicine = Medicine.builder()
                .medicineName(dto.getMedicineName())
                .dosage(dto.getDosage())
                .manufacturerName(dto.getManufacturerName())
                .batchNumber(dto.getBatchNumber())
                .quantity(dto.getQuantity())
                .productionDate(dto.getProductionDate())
                .expirationDate(dto.getExpirationDate())
                .description(dto.getDescription())
                .categoryName(MedicineCategory.valueOf(dto.getCategoryName().toUpperCase()))
                .medicineType(MedicineType.valueOf(dto.getMedicineType().toUpperCase()))
                .build();

        when(medicineRepository.save(any(Medicine.class))).thenReturn(new Medicine()); // Mock the save operation

        medicineService.createMedicine(dto);

        verify(medicineRepository).save(argThat(medicine ->
                medicine.getMedicineName().equals(expectedMedicine.getMedicineName()) &&
                        medicine.getDosage().equals(expectedMedicine.getDosage()) &&
                        medicine.getManufacturerName().equals(expectedMedicine.getManufacturerName()) &&
                        medicine.getBatchNumber().equals(expectedMedicine.getBatchNumber()) &&
                        medicine.getQuantity() == expectedMedicine.getQuantity() &&
                        medicine.getProductionDate().equals(expectedMedicine.getProductionDate()) &&
                        medicine.getExpirationDate().equals(expectedMedicine.getExpirationDate()) &&
                        medicine.getDescription().equals(expectedMedicine.getDescription()) &&
                        medicine.getCategoryName().equals(expectedMedicine.getCategoryName()) &&
                        medicine.getMedicineType().equals(expectedMedicine.getMedicineType())
        ));
    }
    @Test
    public void testCreateMedicineWithNullFields() {
        MedicineCreateDTO dto = MedicineCreateDTO.builder()
                .medicineName("Paracetamol")
                .dosage(null) // Null field
                .manufacturerName("Pharma Inc.")
                .batchNumber("B4321")
                .quantity(100L)
                .productionDate(LocalDate.now().minusDays(30))
                .expirationDate(LocalDate.now().plusDays(365))
                .description(null) // Null field
                .categoryName("ANTIDEPRESSANTS")
                .medicineType("Tablet")
                .build();

        when(medicineRepository.save(any(Medicine.class))).thenReturn(new Medicine());

        medicineService.createMedicine(dto);

        verify(medicineRepository).save(argThat(medicine ->
                Objects.equals(medicine.getMedicineName(), dto.getMedicineName()) &&
                        Objects.equals(medicine.getDosage(), dto.getDosage()) && // Correctly handles nulls
                        Objects.equals(medicine.getManufacturerName(), dto.getManufacturerName()) &&
                        Objects.equals(medicine.getBatchNumber(), dto.getBatchNumber()) &&
                        medicine.getQuantity() == dto.getQuantity() &&
                        Objects.equals(medicine.getProductionDate(), dto.getProductionDate()) &&
                        Objects.equals(medicine.getExpirationDate(), dto.getExpirationDate()) &&
                        Objects.equals(medicine.getDescription(), dto.getDescription()) &&
                        Objects.equals(medicine.getCategoryName(), MedicineCategory.valueOf(dto.getCategoryName().toUpperCase())) &&
                        Objects.equals(medicine.getMedicineType(), MedicineType.valueOf(dto.getMedicineType().toUpperCase()))
        ));
    }
    @Test
    public void testCreateMedicineWithInvalidDates() {
        MedicineCreateDTO dto = MedicineCreateDTO.builder()
                .medicineName("Ibuprofen")
                .dosage("200mg")
                .manufacturerName("HealthCorp")
                .batchNumber("B5678")
                .quantity(200L)
                .productionDate(LocalDate.now().plusDays(30)) // Future production date (Invalid)
                .expirationDate(LocalDate.now().minusDays(365)) // Past expiration date (Invalid)
                .description("Anti-inflammatory")
                .categoryName("NSAID")
                .medicineType("Capsule")
                .build();

        assertThrows(CustomeException.class, () -> {
            medicineService.createMedicine(dto);
        });

        verify(medicineRepository, never()).save(any(Medicine.class));
    }
    @Test
    public void testUpdateMedicineSuccess() {
        MedicineUpdateDTO dto = MedicineUpdateDTO.builder()
                .id(1L)
                .medicineName("Aspirin")
                .dosage("500mg")
                .manufacturerName("Pharma Inc.")
                .batchNumber("B1234")
                .quantity(100L)
                .productionDate(LocalDate.now().minusDays(30))
                .expirationDate(LocalDate.now().plusDays(365))
                .description("Pain reliever")
                .categoryName("ANTIDEPRESSANTS")
                .medicineType("Tablet")
                .build();

        Medicine existingMedicine = new Medicine();
        existingMedicine.setId(dto.getId());

        when(medicineRepository.findById(dto.getId())).thenReturn(Optional.of(existingMedicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(existingMedicine);

        medicineService.updateMedicine(dto);

        verify(medicineRepository).save(argThat(medicine ->
                medicine.getId() == dto.getId() && // Primitive long comparison
                        medicine.getMedicineName().equals(dto.getMedicineName()) &&
                        medicine.getDosage().equals(dto.getDosage()) &&
                        medicine.getManufacturerName().equals(dto.getManufacturerName()) &&
                        medicine.getBatchNumber().equals(dto.getBatchNumber()) &&
                        medicine.getQuantity() == dto.getQuantity() && // Primitive long comparison
                        medicine.getProductionDate().equals(dto.getProductionDate()) &&
                        medicine.getExpirationDate().equals(dto.getExpirationDate()) &&
                        medicine.getDescription().equals(dto.getDescription()) &&
                        medicine.getCategoryName().equals(EnumValidation.parseMedicineCategory(dto.getCategoryName())) &&
                        medicine.getMedicineType().equals(EnumValidation.parseMedicineType(dto.getMedicineType()))
        ));
    }
    @Test
    public void testUpdateMedicineNonExistent() {
        MedicineUpdateDTO dto = MedicineUpdateDTO.builder()
                .id(1L)
                .medicineName("Ibuprofen")
                .dosage("200mg")
                .manufacturerName("HealthCorp")
                .batchNumber("B5678")
                .quantity(200L)
                .productionDate(LocalDate.now().minusDays(30))
                .expirationDate(LocalDate.now().plusDays(365))
                .description("Anti-inflammatory")
                .categoryName("ANTIDEPRESSANTS")
                .medicineType("Capsule")
                .build();

        when(medicineRepository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> medicineService.updateMedicine(dto));

        verify(medicineRepository, never()).save(any(Medicine.class));
    }


}
