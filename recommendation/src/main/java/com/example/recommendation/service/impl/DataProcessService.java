package com.example.recommendation.service.impl;

import com.example.recommendation.dto.request.HealthInfoDTO;
import com.example.recommendation.service.IDataProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataProcessService implements IDataProcessService {
    private final ProgressService progressService;
    private final RecommendationService recommendationService;
    public DataProcessService(ProgressService progressService, RecommendationService recommendationService) {
        this.progressService = progressService;
        this.recommendationService = recommendationService;
    }

    /**
     * This method is responsible for sending health data to the Recommendation, Progress
     * for data processing. It calculates recommendations based on multiple algorithms and stores the results in the database.
     *
     * @param healthInfoDTO An object containing health-related information to be processed.
     * @return void
     */
    public void importHealthData(HealthInfoDTO healthInfoDTO){
        progressService.createProgress(healthInfoDTO);
        recommendationService.createRecommendation(healthInfoDTO);
    }
}
