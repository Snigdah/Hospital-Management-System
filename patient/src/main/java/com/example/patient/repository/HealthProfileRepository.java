package com.example.patient.repository;

import com.example.patient.model.HealthProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthProfileRepository extends JpaRepository<HealthProfile,Long> {
    Optional<HealthProfile> findByUserId(long userId);
}
