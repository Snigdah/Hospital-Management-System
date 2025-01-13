package com.example.recommendation.service;

import com.example.recommendation.dto.request.HealthInfoDTO;

public interface IDataProcessService {
    public void importHealthData(HealthInfoDTO healthInfoDTO);
}
