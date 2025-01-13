package com.example.administrative.controller;

import com.example.administrative.dto.request.CreateDoctorDTO;
import com.example.administrative.dto.request.RoomAvailableDTO;
import com.example.administrative.dto.response.ProxyResponse;
import com.example.administrative.dto.response.ResponseDoctorDTO;
import com.example.administrative.dto.response.RoomInfoDTO;
import com.example.administrative.services.IProxyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/administrative/proxy")
public class ProxyController {

    private final IProxyService proxyService;

    public ProxyController(IProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @PutMapping("/roomAvailability")
    public Mono<ProxyResponse> importUserHealthData(@Valid @RequestBody RoomAvailableDTO roomAvailableDTO) {

        return Mono.fromRunnable(() -> proxyService.changeRoomAvailability(roomAvailableDTO))
                .thenReturn(new ProxyResponse("Data received successfully"));
    }

    @PostMapping("/assignDoctor")
    public ResponseDoctorDTO assignDoctorDeptAndRoom (@Valid @RequestBody CreateDoctorDTO createDoctorDTO){
        return proxyService.assignRoomToDoctor(createDoctorDTO);
    }

    @GetMapping("/roomInfo/{roomId}")
    public RoomInfoDTO getRoomInformation (@PathVariable long roomId){
        return proxyService.getRoomInfo(roomId);
    }

}
