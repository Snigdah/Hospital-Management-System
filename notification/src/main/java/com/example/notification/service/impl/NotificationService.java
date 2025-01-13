package com.example.notification.service.impl;

import com.example.notification.dto.request.NotificationCreateDTO;
import com.example.notification.model.Notification;
import com.example.notification.model.enums.Role;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
public class NotificationService implements INotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Save Notification in DB
    @Override
    public void saveNotification(NotificationCreateDTO createDTO) {
        LocalDateTime appointmentDateTime = createCombinedDateTime(createDTO.getAppointmentDate(), createDTO.getAppointmentTime());
        createAndSaveNotification(createDTO.getPatientId(), appointmentDateTime, Role.PATIENT);

        LocalDateTime doctorStartTime = createCombinedDateTime(createDTO.getAppointmentDate(), createDTO.getDoctorStartTime());
        createAndSaveDoctorNotificationIfNotExists(createDTO.getDoctorId(), doctorStartTime);
    }

    // Reduce time from actual time 2 minutes
    private LocalDateTime createCombinedDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).minusMinutes(2);
    }

    // Map DTO to entity and save in database
    private void createAndSaveNotification(Long userId, LocalDateTime dateTime, Role role) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setAppointmentTime(time);
        notification.setAppointmentDate(date);
        notification.setRole(role);

        notificationRepository.save(notification);
        log.info("Notification saved for {} with ID {}", role, userId);
    }

    // Check if doctor already exists or not for that day
    private void createAndSaveDoctorNotificationIfNotExists(Long doctorId, LocalDateTime doctorStartTime) {
        LocalDate date = doctorStartTime.toLocalDate();

        boolean exists = notificationRepository.findByUserIdAndAppointmentDate(doctorId, date).isPresent();
        if (!exists) {
            createAndSaveNotification(doctorId, doctorStartTime, Role.DOCTOR);
        } else {
            log.info("Notification for doctor with ID {} on {} already exists.", doctorId, date);
        }
    }
}
