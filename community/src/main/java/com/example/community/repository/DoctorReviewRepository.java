package com.example.community.repository;

import com.example.community.model.DoctorReview;
import com.example.community.model.HospitalReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorReviewRepository extends JpaRepository<DoctorReview, Long> {
    Optional<DoctorReview> findByUserIdAndDoctorId(long userId, long doctorId);
    List<DoctorReview> findAllByDoctorIdOrderByUserIdAsc(long doctorId);
}
