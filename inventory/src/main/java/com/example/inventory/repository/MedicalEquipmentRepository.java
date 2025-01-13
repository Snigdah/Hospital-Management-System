package com.example.inventory.repository;

import com.example.inventory.entity.MedicalEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalEquipmentRepository extends JpaRepository<MedicalEquipment, Long> {
}
