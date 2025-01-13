package com.example.doctor.utils;

public class Constants {

    public static final long MAX_ROOMS_PER_DEPARTMENT = 1000;
    public static final long STARTING_ROOM_NUMBER = 1000;

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final String MESSAGE_KEY = "message";
    public static final String STATUS_KEY = "status";
    public static final String DATA_KEY = "data";
    public static final int SUCCESS_STATUS_CODE = 200;
    public static final int MAX_RETRY_ATTEMPTS = 6;

    // Roles
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ADMINISTRATIVE = "ADMINISTRATIVE";
    public static final String ROLE_PATIENT = "PATIENT";
    public static final String ROLE_DOCTOR = "DOCTOR";
}
