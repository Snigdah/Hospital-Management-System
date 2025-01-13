package com.example.recommendation.repository;

import com.example.recommendation.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    //@Query("SELECT p FROM Progress p WHERE p.userId = :userId ORDER BY p.id DESC")
    List<Progress> findTop5ByUserIdOrderByIdDesc( long userId);
}
