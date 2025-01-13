package com.example.recommendation.utils;

public class Constants {
    public static final long MAX_ROOMS_PER_DEPARTMENT = 1000;
    public static final long STARTING_ROOM_NUMBER = 1000;

    public static final double MALE_BMR_CONSTANT = 88.362;
    public static final double MALE_BMR_WEIGHT_COEFFICIENT = 13.397;
    public static final double MALE_BMR_HEIGHT_COEFFICIENT = 4.799;
    public static final double MALE_BMR_AGE_COEFFICIENT = 5.677;

    public static final double FEMALE_BMR_CONSTANT = 447.593;
    public static final double FEMALE_BMR_WEIGHT_COEFFICIENT = 9.247;
    public static final double FEMALE_BMR_HEIGHT_COEFFICIENT = 3.098;
    public static final double FEMALE_BMR_AGE_COEFFICIENT = 4.330;

    public static final String MESSAGE_KEY = "message";
    public static final String STATUS_KEY = "status";
    public static final String DATA_KEY = "data";
    public static final double LOWER_NORMAL_BMI  = 18.5;
    public static final double UPPER_NORMAL_BMI  = 24.9;
    public static final double MARGIN  = 0.5;
    public static final int SUCCESS_STATUS_CODE = 200;
    public static final int MAX_RETRY_ATTEMPTS = 6;

    // Roles
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ADMINISTRATIVE = "ADMINISTRATIVE";
    public static final String ROLE_PATIENT = "PATIENT";
    public static final String ROLE_DOCTOR = "DOCTOR";
}
