package com.example.doctor.utils;

import com.example.doctor.exception.CustomeException;
import com.example.doctor.model.enums.AppointmentType;
import com.example.doctor.model.enums.MedicalDegree;
import com.example.doctor.model.enums.MedicalDesignation;
import org.springframework.http.HttpStatus;

public class EnumValidation {
    public static AppointmentType parseAppointmentType(String userAppointmentType) {
        try {
            return AppointmentType.valueOf(userAppointmentType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid Appointment Type. Supported Appointment Type are IN_PERSON and TELEMEDICINE ");
        }
    }

    public static MedicalDegree parseMedicalDegree(String medicalDegreeType) {
        try {
            return MedicalDegree.valueOf(medicalDegreeType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid Degree. Supported Degree are MBBS, MD, DO, BDS, MS ");
        }
    }

    public static MedicalDesignation parseMedicalDesignation(String medicalDesignationType) {
        try {
            return MedicalDesignation.valueOf(medicalDesignationType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid Designation. Supported Designation SENIOR_CONSULTANT,  PROFESSOR, ASSOCIATE_PROFESSOR, JUNIOR_DOCTOR, MEDICAL_OFFICER, SURGEON");
        }
    }
}
