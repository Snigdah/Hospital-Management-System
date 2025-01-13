package com.example.inventory.utils;

import com.example.inventory.entity.enums.EquipmentCategory;
import com.example.inventory.entity.enums.MedicineCategory;
import com.example.inventory.entity.enums.MedicineType;
import com.example.inventory.exception.CustomeException;
import org.springframework.http.HttpStatus;

public class EnumValidation {
    public static MedicineCategory parseMedicineCategory(String medicineCategory) {
        try {
            return MedicineCategory.valueOf(medicineCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid MedicineCategory. Supported Category are ANTIDEPRESSANTS, ANTIPSYCHOTICS, STEROIDS, ANTIHISTAMINES, ANTIBIOTICS, ANTIVIRALS, VACCINES");
        }
    }

    public static EquipmentCategory parseEquipmentCategory(String equipmentCategory) {
        try {
            return EquipmentCategory.valueOf(equipmentCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid EquipmentCategory. Supported Category are LABORATORY, EMERGENCY_RESUSCITATION, DIAGNOSTIC, SURGICAL");
        }
    }

    public static MedicineType parseMedicineType(String medicineType) {
        try {
            return MedicineType.valueOf(medicineType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid MedicineType. Supported types are TABLET, CAPSULE, SYRUP, INJECTION, DROPS");
        }
    }
}
