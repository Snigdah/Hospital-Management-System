package com.example.doctor.services.impl;

import com.example.doctor.model.Doctor;
import com.example.doctor.utils.EnumValidation;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {
    public static Specification<Doctor> withDynamicQuery(String department,
                                                         String medicalDesignation,
                                                         String medicalDegree,
                                                         Long yearOfExp) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (department != null && !department.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("department"), department));
            }
            if (medicalDesignation != null && !medicalDesignation.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("medicalDesignation"), EnumValidation.parseMedicalDesignation(medicalDesignation)));
            }
            if (medicalDegree != null && !medicalDegree.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("medicalDegree"), EnumValidation.parseMedicalDegree(medicalDegree)));
            }
            if (yearOfExp != null) {
                predicates.add(criteriaBuilder.ge(root.get("yearOfExp"), yearOfExp));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
