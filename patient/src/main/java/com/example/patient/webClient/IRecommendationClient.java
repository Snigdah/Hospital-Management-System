package com.example.patient.webClient;

import com.example.patient.dto.request.HealthInfoDTO;
import com.example.patient.dto.response.ProxyResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface IRecommendationClient {
    public Mono<ProxyResponse> importUserHealthData(@Valid @RequestBody HealthInfoDTO healthInfoDTO);
}
