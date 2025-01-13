package com.example.administrative.services.impl;

import com.example.administrative.dto.response.DashboardCountDTO;
import com.example.administrative.dto.response.DoctorCountDTO;
import com.example.administrative.dto.response.PatientCountDTO;
import com.example.administrative.feignclient.DoctorServiceClient;
import com.example.administrative.feignclient.PatientServiceClient;
import com.example.administrative.repository.DepartmentRepository;
import com.example.administrative.services.IDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardService implements IDashboardService {
    private final DoctorServiceClient doctorServiceClient;
    private final PatientServiceClient patientServiceClient;
    private final DepartmentRepository departmentRepository;
    public DashboardService(DoctorServiceClient doctorServiceClient, PatientServiceClient patientServiceClient, DepartmentRepository departmentRepository) {
        this.doctorServiceClient = doctorServiceClient;
        this.patientServiceClient = patientServiceClient;
        this.departmentRepository = departmentRepository;
    }

    // Get Dashboard information
    public DashboardCountDTO getDashboardCount() {
        // Call Doctor Microservice for fetch data
        DoctorCountDTO doctor = doctorServiceClient.getDoctorCount();

        // Call Patient Microservice for fetch data
        PatientCountDTO patient = patientServiceClient.getPatientCount();

        // Set data to DTO
        DashboardCountDTO dashboardCount = new DashboardCountDTO();
        dashboardCount.setTotalDoctor(doctor.getTotalDoctor());
        dashboardCount.setTotalPatient(patient.getTotalPatient());
        dashboardCount.setTotalDonor(patient.getTotalDonor());
        dashboardCount.setTotalDepartment(departmentRepository.count());

        return dashboardCount;
    }
}
