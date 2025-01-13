package com.example.patient.webClient.impl;

import com.example.patient.dto.request.HealthInfoDTO;
import com.example.patient.dto.response.ProxyResponse;
import com.example.patient.security.CustomWebAuthenticationDetails;
import com.example.patient.utils.Constants;
import com.example.patient.webClient.IRecommendationClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class RecommendationClient implements IRecommendationClient {
    private final WebClient.Builder webClientBuilder;
    public RecommendationClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder.baseUrl("http://localhost:8209");
    }

    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "notificationFallback")
    @Override
    public Mono<ProxyResponse> importUserHealthData(@Valid @RequestBody HealthInfoDTO healthInfoDTO){
        String jwtToken = extractJwtToken();

        return webClientBuilder
                .build()
                .post()
                .uri("/api/v2/recommendation/proxy/import-healthInfo")
                .header("Authorization",jwtToken)
                .bodyValue(healthInfoDTO)
                .retrieve()
                .bodyToMono(ProxyResponse.class)
                .retryWhen(Retry.backoff(Constants.MAX_RETRY_ATTEMPTS, Duration.ofSeconds(20))
                        .filter(this::isRetryableException))
                .onErrorResume(ex -> recommendationFallback(healthInfoDTO, ex));
    }

    private boolean isRetryableException(Throwable ex) {
        // Retry on specific server errors (5xx status codes)
        if (ex instanceof WebClientResponseException responseException) {
            return responseException.getStatusCode().is5xxServerError();
        }
        // Retry on connection issues
        if (ex instanceof WebClientRequestException) {
            return true;
        }
        return false;
    }

    private Mono<? extends ProxyResponse> recommendationFallback(@Valid @RequestBody HealthInfoDTO healthInfoDTO , Throwable ex) {
        ProxyResponse response = new ProxyResponse();
        response.setResponse("Fallback: Service can't receive data. Server down or error occurred.");
        return Mono.just(response);
    }

    private String extractJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof CustomWebAuthenticationDetails) {
            CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
            return "Bearer " + details.getJwtToken();
        }
        return null;
    }
}
