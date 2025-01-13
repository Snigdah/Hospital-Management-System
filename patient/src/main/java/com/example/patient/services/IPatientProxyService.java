package com.example.patient.services;

import com.example.patient.dto.request.UserIdDTO;
import com.example.patient.dto.response.PatientCountDTO;
import com.example.patient.dto.response.PatientProfileDTO;
import com.example.patient.dto.response.UserInfoDTO;

import java.util.List;

public interface IPatientProxyService {
    public PatientProfileDTO getPatientProfileByUserId(long userId);
    public List<UserInfoDTO> getUserInfoList(List<UserIdDTO> userIdDTOs);
    public PatientCountDTO getPatientCount();
}
