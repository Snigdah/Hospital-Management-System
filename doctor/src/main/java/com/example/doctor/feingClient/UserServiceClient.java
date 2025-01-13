package com.example.doctor.feingClient;

import com.example.doctor.dto.request.UserIdDTO;
import com.example.doctor.dto.response.UserInfoDTO;
import com.example.doctor.feingClient.handleFeingException.CustomFeignErrorDecoder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "PATIENT-SERVICE", configuration = CustomFeignErrorDecoder.class)
public interface UserServiceClient {
    @CircuitBreaker(name = "CircuitBreakerService")
    @PostMapping("/api/v1/patient/proxy/userinfo")
    public List<UserInfoDTO> getUserInfo(@RequestBody List<UserIdDTO> userIdDTOs);
}
