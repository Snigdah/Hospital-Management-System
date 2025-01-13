package com.example.inventory.entity;

import com.example.inventory.entity.enums.MedicineCategory;
import com.example.inventory.entity.enums.MedicineType;
import jakarta.persistence.*;
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
@Table(name = "_medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "medicineName", nullable = false)
    private String medicineName;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private String manufacturerName;

    @Column(nullable = false)
    private String batchNumber;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private LocalDate productionDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicineCategory categoryName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicineType medicineType;
}
