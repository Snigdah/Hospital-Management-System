package com.example.administrative.feignclient;

import com.example.administrative.dto.response.PatientCountDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "PATIENT-SERVICE", configuration = CustomFeignErrorDecoder.class)
public interface PatientServiceClient {
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "getPatientCountFallback")
    @GetMapping("/api/v1/patient/proxy/patientCount")
    public PatientCountDTO getPatientCount();

    default PatientCountDTO getPatientCount(Throwable throwable){
        PatientCountDTO result = new PatientCountDTO();
        result.setTotalPatient(0);
        result.setTotalDonor(0);
        return result;
    }
}
