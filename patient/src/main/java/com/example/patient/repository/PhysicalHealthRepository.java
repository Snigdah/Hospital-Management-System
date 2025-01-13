package com.example.patient.repository;

import com.example.patient.model.PhysicalHealth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalHealthRepository extends JpaRepository<PhysicalHealth, Long> {
}
