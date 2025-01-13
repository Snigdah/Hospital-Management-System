package com.example.community.repository;

import com.example.community.model.HospitalReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface HospitalReviewRepository extends JpaRepository<HospitalReview, Long> {
    Optional<HospitalReview> findByUserId(long userId);
    List<HospitalReview> findAllByOrderByUserIdAsc();
}
