package com.example.community.feingClient;

import com.example.community.feingClient.handleFeingException.CustomFeignErrorDecoder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DOCTOR-SERVICE", configuration = CustomFeignErrorDecoder.class)
public interface DoctorServiceClient {
    @CircuitBreaker(name = "CircuitBreakerService")
    @GetMapping("/api/v2/doctor/proxy/doctorExists/{doctorId}")
    public boolean doctorExists(@PathVariable long doctorId);
}
