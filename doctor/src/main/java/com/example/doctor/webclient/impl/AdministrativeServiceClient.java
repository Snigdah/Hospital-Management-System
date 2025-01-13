package com.example.doctor.webclient.impl;

import com.example.doctor.dto.request.RoomAvailableDTO;
import com.example.doctor.dto.response.ProxyResponse;
import com.example.doctor.utils.Constants;
import com.example.doctor.webclient.IAdministrativeServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class AdministrativeServiceClient implements IAdministrativeServiceClient {
    private final WebClient.Builder webClientBuilder;

    public AdministrativeServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder.baseUrl("http://localhost:8202");
    }

    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "roomDataFallback")
    @Override
    public Mono<ProxyResponse> changeRoomAvailability(RoomAvailableDTO roomAvailableDTO) {
        return webClientBuilder
                .build()
                .put()
                .uri("/api/v2/administrative/proxy/roomAvailability")
                .bodyValue(roomAvailableDTO)
                .retrieve()
                .bodyToMono(ProxyResponse.class)
                .retryWhen(Retry.backoff(Constants.MAX_RETRY_ATTEMPTS, Duration.ofSeconds(20))
                        .filter(this::isRetryableException)) // Retry with a 20-second delay
                .onErrorResume(ex -> roomDataFallback(roomAvailableDTO, ex));
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

    private Mono<? extends ProxyResponse> roomDataFallback(RoomAvailableDTO roomAvailableDTO , Throwable ex) {
        ProxyResponse response = new ProxyResponse();
        response.setResponse("Fallback: Service can't receive data. Server down or error occurred.");
        return Mono.just(response);
    }

}
