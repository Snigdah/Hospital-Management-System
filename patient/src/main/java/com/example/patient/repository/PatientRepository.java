package com.example.patient.repository;

import com.example.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    Optional<Patient> findByUserId(long userId);
    List<Patient> findByUserIdInOrderByUserIdAsc(List<Long> userIds);
    long countByInterestedToBloodDonate(boolean interested);
}
