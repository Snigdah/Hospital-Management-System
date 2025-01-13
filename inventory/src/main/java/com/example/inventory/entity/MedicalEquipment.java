package com.example.inventory.entity;

import jakarta.persistence.*;
import com.example.inventory.entity.enums.EquipmentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MedicalEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String equipmentName;

    @Column(nullable = false)
    private String manufacturerName;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private LocalDate lastMaintenanceDate;

    @Enumerated(EnumType.STRING)
    private EquipmentCategory category;
}
