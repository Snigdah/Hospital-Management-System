package com.example.community.service;

import com.example.community.dto.request.HospitalReviewCreateDTO;
import com.example.community.dto.response.HospitalReviewDTO;

import java.util.List;

public interface IHospitalReviewService {
    public void createHospitalReview(long userId, HospitalReviewCreateDTO dto);
    public void deleteReview(long reviewId, long userId);
    public List<HospitalReviewDTO> getAllReviewsWithUserName();
}
