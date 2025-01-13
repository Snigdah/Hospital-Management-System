package com.example.doctor.webclient;

import com.example.doctor.dto.request.NotificationCreateDTO;
import com.example.doctor.dto.response.ProxyResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface IPushNotificationServiceClient {
    public Mono<ProxyResponse> saveNotification(@Valid @RequestBody NotificationCreateDTO createDTO);
}
