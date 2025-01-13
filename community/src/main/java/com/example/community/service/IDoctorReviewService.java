package com.example.community.service;

import com.example.community.dto.request.DoctorReviewCreateDTO;
import com.example.community.dto.response.DoctorReviewDTO;

import java.util.List;

public interface IDoctorReviewService {
    public void createDoctorReview(DoctorReviewCreateDTO createDTO, long userId);
    public List<DoctorReviewDTO> getAllReviewForSpecificDoctor(long doctorId);
    public void deleteDoctorReview(long userId, long doctorId);
}
