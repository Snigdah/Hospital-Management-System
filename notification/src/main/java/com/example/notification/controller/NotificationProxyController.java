package com.example.notification.controller;

import com.example.notification.dto.request.NotificationCreateDTO;
import com.example.notification.dto.response.ProxyResponse;
import com.example.notification.response.ResponseHandler;
import com.example.notification.service.INotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/notification")
public class NotificationProxyController {
    private final INotificationService notificationService;

    public NotificationProxyController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/save")
    public Mono<ProxyResponse> saveNotification(@Valid @RequestBody NotificationCreateDTO createDTO) {
        return Mono.fromRunnable(() ->   notificationService.saveNotification(createDTO))
                .thenReturn(new ProxyResponse("Data received successfully"));
    }
}
