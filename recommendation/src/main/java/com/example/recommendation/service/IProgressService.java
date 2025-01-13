package com.example.recommendation.service;

import com.example.recommendation.dto.response.ProgressDTO;
import com.example.recommendation.dto.response.RecommendationDTO;

import java.util.List;
import java.util.Optional;

public interface IProgressService {
    public List<ProgressDTO> getLastFiveProgressRecords(long userId);
    public RecommendationDTO getRecommendationByUserId(long userId);
}
