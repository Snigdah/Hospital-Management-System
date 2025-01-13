package com.example.doctor.repository;

import com.example.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    List<Doctor> findByDeptId(long deptId);
    Optional<Doctor> findByUserId(Long userId);
}
