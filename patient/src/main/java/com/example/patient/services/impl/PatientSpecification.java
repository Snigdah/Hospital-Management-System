package com.example.patient.services.impl;

import com.example.patient.model.Patient;
import com.example.patient.utils.EnumValidation;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
public class PatientSpecification {
    public static Specification<Patient> withDynamicQuery(String bloodGroup) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (bloodGroup != null && !bloodGroup.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("bloodGroup"), EnumValidation.parseBloodGroup(bloodGroup)));
            }
            predicates.add(criteriaBuilder.isTrue(root.get("interestedToBloodDonate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
