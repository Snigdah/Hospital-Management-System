package com.example.doctor.feingClient;

import com.example.doctor.dto.request.CreateDoctorDTO;
import com.example.doctor.dto.request.RoomAvailableDTO;
import com.example.doctor.dto.response.ProxyResponse;
import com.example.doctor.dto.response.ResponseDoctorDTO;
import com.example.doctor.dto.response.RoomInfoDTO;
import com.example.doctor.feingClient.handleFeingException.CustomFeignErrorDecoder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ADMINISTRATIVE-SERVICE", configuration = CustomFeignErrorDecoder.class)
public interface AdminServiceClient {
    @CircuitBreaker(name = "CircuitBreakerService")
    @PostMapping("/api/v2/administrative/proxy/assignDoctor")
    public ResponseDoctorDTO assignDoctorDeptAndRoom (@Valid @RequestBody CreateDoctorDTO createDoctorDTO);

    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "getRoomInformationFallback")
    @GetMapping("/api/v2/administrative/proxy/roomInfo/{roomId}")
    public RoomInfoDTO getRoomInformation (@PathVariable long roomId);

    // FallBack Method for RoomInformationFailed
    default public RoomInfoDTO getRoomInformationFallback(@PathVariable long roomId, Throwable throwable) {
        // This is the fallback method that will be called when CircuitBreaker is open or an exception occurs.
        return RoomInfoDTO
                .builder()
                .roomNumber(0)
                .deptName("Dummy Dept Name")
                .roomType("Dummy Room Type")
                .build();
    }
}
