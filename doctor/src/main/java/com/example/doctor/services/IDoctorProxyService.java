package com.example.doctor.services;

import com.example.doctor.dto.response.DoctorCountDTO;

public interface IDoctorProxyService {
    public boolean isDoctorExistsOrNot(long userId);
    public DoctorCountDTO totalDoctorCount();
}
