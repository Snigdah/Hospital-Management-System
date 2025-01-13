package com.example.notification.service.impl;

import com.example.notification.dto.response.NotificationDTO;
import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PushNotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    public PushNotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(cron = "0 * * * * *") // This will run at the start of every minute
    public void sendUpcomingAppointmentNotifications() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0); // This ensures we are checking times disregarding seconds
        int hour = now.getHour();
        int minute = now.getMinute();

        log.info("date {}, hour {}, minute {}", today, hour, minute);

        // Fetch notifications that are scheduled for the current date and time (ignoring seconds)
        List<Notification> dueNotifications = notificationRepository
                .findAllByAppointmentDateAndTime(today, hour, minute);

        for (Notification notification : dueNotifications) {
            String destination = "/user/" + notification.getUserId() + "/notification";
            String message = "Your appointment is today within 2 minutes";
            String isoDate = DATE_FORMATTER.format(new Date());

            // Send the notification to the specific user
            messagingTemplate.convertAndSend(destination, new NotificationDTO(message,isoDate));
            log.info("destination {} message {}", destination, message);
        }
    }

}



