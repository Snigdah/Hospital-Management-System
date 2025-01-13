package com.example.recommendation.controller;

import com.example.recommendation.dto.request.HealthInfoDTO;
import com.example.recommendation.dto.response.ProxyResponse;
import com.example.recommendation.service.IDataProcessService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/recommendation/proxy")
public class RecommendationProxyController {

    private final IDataProcessService dataProcessService;

    public RecommendationProxyController(IDataProcessService dataProcessService) {
        this.dataProcessService = dataProcessService;
    }

    @PostMapping("/import-healthInfo")
        public Mono<ProxyResponse> importUserHealthData(@Valid @RequestBody HealthInfoDTO healthInfoDTO) {

        return Mono.fromRunnable(() -> dataProcessService.importHealthData(healthInfoDTO))
                .thenReturn(new ProxyResponse("Data received successfully"));
    }
}
