package com.example.notification.service;

import com.example.notification.dto.request.NotificationCreateDTO;

public interface INotificationService {
    public void saveNotification(NotificationCreateDTO createDTO);
}
