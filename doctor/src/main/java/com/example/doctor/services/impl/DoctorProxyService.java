package com.example.doctor.services.impl;

import com.example.doctor.dto.response.DoctorCountDTO;
import com.example.doctor.repository.DoctorRepository;
import com.example.doctor.services.IDoctorProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DoctorProxyService implements IDoctorProxyService {
    private final DoctorRepository doctorRepository;

    public DoctorProxyService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Check Doctor Exists or not
    @Override
    public boolean isDoctorExistsOrNot(long userId){
        return doctorRepository.findByUserId(userId).isPresent();
    }

    public DoctorCountDTO totalDoctorCount(){
        // Count total number of doctor
        DoctorCountDTO response = new DoctorCountDTO();
        response.setTotalDoctor(doctorRepository.count());

        return response;
    }
}
