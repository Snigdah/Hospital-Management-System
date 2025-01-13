package com.example.administrative.feignclient;

import com.example.administrative.dto.response.DoctorCountDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "DOCTOR-SERVICE", configuration = CustomFeignErrorDecoder.class)
public interface DoctorServiceClient {
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "getDoctorCountFallback")
    @GetMapping("/api/v2/doctor/proxy/doctorCount")
    public DoctorCountDTO getDoctorCount();

    default DoctorCountDTO getDoctorCount( Throwable throwable){
        DoctorCountDTO result = new DoctorCountDTO();
        result.setTotalDoctor(20);
        return result;
    }
}
