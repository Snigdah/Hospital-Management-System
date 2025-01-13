package com.example.inventory.service;

import com.example.inventory.dto.request.MedicalEquipmentCreateDTO;
import com.example.inventory.dto.request.MedicalEquipmentUpdateDTO;
import com.example.inventory.entity.MedicalEquipment;
import com.example.inventory.entity.enums.EquipmentCategory;
import com.example.inventory.exception.CustomeException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.MedicalEquipmentRepository;
import com.example.inventory.service.impl.MedicalEquipmentService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import com.example.inventory.utils.EnumValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class MedicalEquipmentServiceTest {

    @Mock
    private MedicalEquipmentRepository medicalEquipmentRepository;

    @InjectMocks
    private MedicalEquipmentService medicalEquipmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLastMaintenanceDateInThePast() {
        LocalDate lastMaintenanceDate = LocalDate.now().minusDays(10); // Past date

        assertDoesNotThrow(
                () -> medicalEquipmentService.validateLastMaintenanceDate(lastMaintenanceDate),
                "No exception expected for last maintenance date in the past"
        );
    }

    @Test
    public void testLastMaintenanceDateIsToday() {
        LocalDate lastMaintenanceDate = LocalDate.now(); // Today's date

        CustomeException thrown = assertThrows(
                CustomeException.class,
                () -> medicalEquipmentService.validateLastMaintenanceDate(lastMaintenanceDate),
                "Expected an exception for last maintenance date being today"
        );

        assertTrue(thrown.getMessage().contains("Last maintenance date must be in the past."));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    public void testLastMaintenanceDateInTheFuture() {
        LocalDate lastMaintenanceDate = LocalDate.now().plusDays(10); // Future date

        CustomeException thrown = assertThrows(
                CustomeException.class,
                () -> medicalEquipmentService.validateLastMaintenanceDate(lastMaintenanceDate),
                "Expected an exception for future last maintenance date"
        );

        assertTrue(thrown.getMessage().contains("Last maintenance date must be in the past."));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    public void testNullLastMaintenanceDate() {
        assertDoesNotThrow(
                () -> medicalEquipmentService.validateLastMaintenanceDate(null),
                "No exception expected for null last maintenance date"
        );
    }

    @Test
    public void testCreateMedicalEquipmentSuccess() {
        MedicalEquipmentCreateDTO dto = MedicalEquipmentCreateDTO.builder()
                .equipmentName("Ultrasound Machine")
                .manufacturerName("HealthTech")
                .serialNumber("SN123456")
                .quantity(5L)
                .lastMaintenanceDate(LocalDate.now().minusDays(100))
                .category("DIAGNOSTIC")
                .build();

        MedicalEquipment expectedEquipment = MedicalEquipment.builder()
                .equipmentName(dto.getEquipmentName())
                .manufacturerName(dto.getManufacturerName())
                .serialNumber(dto.getSerialNumber())
                .quantity(dto.getQuantity())
                .lastMaintenanceDate(dto.getLastMaintenanceDate())
                .category(EquipmentCategory.valueOf(dto.getCategory().toUpperCase()))
                .build();

        when(medicalEquipmentRepository.save(any(MedicalEquipment.class))).thenReturn(expectedEquipment);

        medicalEquipmentService.createMedicalEquipment(dto);

        verify(medicalEquipmentRepository).save(argThat(equipment ->
                equipment.getEquipmentName().equals(dto.getEquipmentName()) &&
                        equipment.getManufacturerName().equals(dto.getManufacturerName()) &&
                        equipment.getSerialNumber().equals(dto.getSerialNumber()) &&
                        equipment.getQuantity() == dto.getQuantity() &&
                        equipment.getLastMaintenanceDate().equals(dto.getLastMaintenanceDate()) &&
                        equipment.getCategory().equals(EquipmentCategory.valueOf(dto.getCategory().toUpperCase()))
        ));
    }

    @Test
    public void testCreateMedicalEquipmentWithInvalidDate() {
        MedicalEquipmentCreateDTO dto = MedicalEquipmentCreateDTO.builder()
                .equipmentName("X-Ray Machine")
                .manufacturerName("TechCorp")
                .serialNumber("SN789101")
                .quantity(2L)
                .lastMaintenanceDate(LocalDate.now().plusDays(10)) // Future date (Invalid)
                .category("DIAGNOSTIC")
                .build();

        assertThrows(CustomeException.class, () -> {
            medicalEquipmentService.createMedicalEquipment(dto);
        });

        verify(medicalEquipmentRepository, never()).save(any(MedicalEquipment.class));
    }

    @Test
    public void testCreateMedicalEquipmentWithInvalidCategory() {
        MedicalEquipmentCreateDTO dto = MedicalEquipmentCreateDTO.builder()
                .equipmentName("MRI Scanner")
                .manufacturerName("ImagingTech")
                .serialNumber("SN321654")
                .quantity(1L)
                .lastMaintenanceDate(LocalDate.now().minusDays(100))
                .category("INVALID_CATEGORY") // Invalid category
                .build();

        assertThrows(RuntimeException.class, () -> {
            medicalEquipmentService.createMedicalEquipment(dto);
        });

        verify(medicalEquipmentRepository, never()).save(any(MedicalEquipment.class));
    }

    @Test
    public void testUpdateMedicalEquipmentSuccess() {
        MedicalEquipmentUpdateDTO dto = MedicalEquipmentUpdateDTO.builder()
                .id(1L)
                .equipmentName("CT Scanner")
                .manufacturerName("ScanCorp")
                .serialNumber("SN102030")
                .quantity(2L)
                .lastMaintenanceDate(LocalDate.now().minusDays(60))
                .category("DIAGNOSTIC")
                .build();

        MedicalEquipment existingEquipment = new MedicalEquipment();
        existingEquipment.setId(dto.getId());

        when(medicalEquipmentRepository.findById(dto.getId())).thenReturn(Optional.of(existingEquipment));
        when(medicalEquipmentRepository.save(any(MedicalEquipment.class))).thenReturn(existingEquipment);

        medicalEquipmentService.updateMedicalEquipment(dto);

        verify(medicalEquipmentRepository).save(argThat(equipment ->
                equipment.getId() == dto.getId() &&
                        equipment.getEquipmentName().equals(dto.getEquipmentName()) &&
                        equipment.getManufacturerName().equals(dto.getManufacturerName()) &&
                        equipment.getSerialNumber().equals(dto.getSerialNumber()) &&
                        equipment.getQuantity() == dto.getQuantity() &&
                        equipment.getLastMaintenanceDate().equals(dto.getLastMaintenanceDate()) &&
                        equipment.getCategory().equals(EnumValidation.parseEquipmentCategory(dto.getCategory()))
        ));
    }

    @Test
    public void testUpdateMedicalEquipmentNonExistent() {
        MedicalEquipmentUpdateDTO dto = MedicalEquipmentUpdateDTO.builder()
                .id(2L)
                .equipmentName("MRI Machine")
                .manufacturerName("ImagingTech")
                .serialNumber("SN789101")
                .quantity(1L)
                .lastMaintenanceDate(LocalDate.now().minusDays(100))
                .category("DIAGNOSTIC")
                .build();

        when(medicalEquipmentRepository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> medicalEquipmentService.updateMedicalEquipment(dto));

        verify(medicalEquipmentRepository, never()).save(any(MedicalEquipment.class));
    }

    @Test
    public void testDeleteMedicalEquipmentByIdSuccess() {
        long equipmentId = 1L;

        when(medicalEquipmentRepository.existsById(equipmentId)).thenReturn(true);

        medicalEquipmentService.deleteMedicalEquipmentById(equipmentId);

        verify(medicalEquipmentRepository).deleteById(equipmentId);
    }

    @Test
    public void testDeleteMedicalEquipmentByIdNonExistent() {
        long equipmentId = 2L;

        when(medicalEquipmentRepository.existsById(equipmentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            medicalEquipmentService.deleteMedicalEquipmentById(equipmentId);
        });

        verify(medicalEquipmentRepository, never()).deleteById(equipmentId);
    }

}
