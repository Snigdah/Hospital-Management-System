package com.example.inventory.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalEquipmentUpdateDTO {
    @NotNull(message = "Id cannot be null or empty")
    private Long id;

    @NotEmpty(message = "Equipment Name should not be null or empty")
    private String equipmentName;

    @NotEmpty(message = "Manufacturer Name should not be null or empty")
    private String manufacturerName;

    @NotEmpty(message = "SerialNumber should not be null or empty")
    private String serialNumber;

    @NotNull(message = "Quantity should not be null or empty")
    private Long quantity;

    @NotNull(message = "Last MaintenanceDate should not be null or empty")
    private LocalDate lastMaintenanceDate;

    @NotEmpty(message = "Category should not be null or empty")
    private String category;
}
