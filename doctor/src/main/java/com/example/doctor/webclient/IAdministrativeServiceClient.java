package com.example.doctor.webclient;

import com.example.doctor.dto.request.RoomAvailableDTO;
import com.example.doctor.dto.response.ProxyResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface IAdministrativeServiceClient {
    public Mono<ProxyResponse> changeRoomAvailability(@Valid @RequestBody RoomAvailableDTO roomAvailableDTO);
}
