package com.example.doctor.services;

import com.example.doctor.dto.response.DashboardResponseDTO;

public interface IDashboardService {
    public DashboardResponseDTO getDashboardCount(long doctorId);
}
