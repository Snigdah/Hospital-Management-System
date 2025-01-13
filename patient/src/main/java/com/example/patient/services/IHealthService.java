package com.example.patient.services;

import com.example.patient.dto.request.HealthProfileDTO;

public interface IHealthService {

    public void createHealthProfile(long userId, HealthProfileDTO healthProfileDTO);
    public void updateHealthProfile(long userId, HealthProfileDTO healthProfileDTO);
}
