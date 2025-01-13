package com.example.patient.controller;

import com.example.patient.dto.request.UserIdDTO;
import com.example.patient.dto.response.HealthProfileDTO;
import com.example.patient.dto.response.PatientCountDTO;
import com.example.patient.dto.response.PatientProfileDTO;
import com.example.patient.dto.response.UserInfoDTO;
import com.example.patient.services.IPatientProxyService;
import com.example.patient.services.IPatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient/proxy")
public class PatientProxyController {

    private final IPatientProxyService proxyService;
    private final IPatientService patientService;

    public PatientProxyController(IPatientProxyService proxyService, IPatientService patientService) {
        this.proxyService = proxyService;
        this.patientService = patientService;
    }

    @GetMapping("/profile/{userId}")
    public PatientProfileDTO getPatientProfileDTO(@PathVariable long userId) {
        return proxyService.getPatientProfileByUserId(userId);
    }

    @GetMapping("/health/{userId}")
    public HealthProfileDTO getUserHealthInfo(@PathVariable long userId) {
        return patientService.getHealthInfoByUserId(userId);
    }

    //public List<UserInfoDTO> getUserInfoList(List<UserIdDTO> userIdDTOs);

    @PostMapping("/userinfo")
    public List<UserInfoDTO> getUserInfo(@RequestBody List<UserIdDTO> userIdDTOs) {
        return proxyService.getUserInfoList(userIdDTOs);
    }


    @GetMapping("/patientCount")
    public PatientCountDTO getPatientCount(){
        return proxyService.getPatientCount();
    }

}
