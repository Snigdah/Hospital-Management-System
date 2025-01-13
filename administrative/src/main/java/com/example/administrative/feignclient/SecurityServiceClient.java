package com.example.administrative.feignclient;

import com.example.administrative.dto.request.RegisterRequestDTO;
import com.example.administrative.dto.response.AuthenticationResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "SECURITY-SERVICE", configuration = CustomFeignErrorDecoder.class)
public interface SecurityServiceClient {
    @CircuitBreaker(name = "CircuitBreakerService")
    @PostMapping("/api/v2/auth/register")
    public AuthenticationResponseDTO register(@RequestBody RegisterRequestDTO request);
}
