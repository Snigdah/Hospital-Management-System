package com.example.doctor.services;

import com.example.doctor.dto.response.DoctorDTO;
import com.example.doctor.dto.response.DoctorInfoDTO;
import com.example.doctor.model.Doctor;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IDoctorService {
    public List<DoctorDTO> getAllDoctors();
    public List<DoctorDTO> getAllDoctorsByDeptId(long deptId);
    public DoctorInfoDTO getDoctorById(long doctorId);
    public void changeStatus(long userId);
    public List<Doctor> searchDoctors(String department,
                                      String medicalDesignation,
                                      String medicalDegree,
                                      Long yearOfExp,
                                      String sortBy,
                                      Sort.Direction sortDir);
}
