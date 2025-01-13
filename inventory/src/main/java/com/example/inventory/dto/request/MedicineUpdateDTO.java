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
public class MedicineUpdateDTO {
    @NotNull(message = "Id cant be null or empty")
    private Long id;

    @NotEmpty(message = "MedicineName should not be null or empty")
    private String medicineName;

    @NotEmpty(message = "Dosage should not be null or empty")
    private String dosage;

    @NotEmpty(message = "MManufacturer Name should not be null or empty")
    private String manufacturerName;

    @NotEmpty(message = "SerialNumber should not be null or empty")
    private String batchNumber;

    @NotNull(message = "Quantity should not be null or empty")
    private Long quantity;

    @NotNull(message = "Production Date should not be null or empty")
    private LocalDate productionDate;

    @NotNull(message = "Expiration Date should not be null or empty")
    private LocalDate expirationDate;

    @NotEmpty(message = "Description should not be null or empty")
    private String description;

    @NotEmpty(message = "CategoryName should not be null or empty")
    private String categoryName;

    @NotEmpty(message = "MedicineType should not be null or empty")
    private String medicineType;
}
