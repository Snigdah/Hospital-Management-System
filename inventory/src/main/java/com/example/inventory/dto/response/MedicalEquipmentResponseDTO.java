package com.example.inventory.dto.response;

import com.example.inventory.entity.enums.EquipmentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalEquipmentResponseDTO {
    private long id;
    private String equipmentName;
    private String manufacturerName;
    private String serialNumber;
    private long quantity;
    private LocalDate lastMaintenanceDate;
    private EquipmentCategory category;
}
