package com.example.inventory.dto.response;

import com.example.inventory.entity.enums.MedicineCategory;
import com.example.inventory.entity.enums.MedicineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponseDTO {
    private long id;
    private String medicineName;
    private String dosage;
    private String manufacturerName;
    private String batchNumber;
    private long quantity;
    private LocalDate productionDate;
    private LocalDate expirationDate;
    private String description;
    private MedicineCategory categoryName;
    private MedicineType medicineType;
}
