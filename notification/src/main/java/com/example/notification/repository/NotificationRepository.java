package com.example.notification.repository;

import com.example.notification.model.Notification;
import com.example.notification.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByUserIdAndAppointmentDate(long userId, LocalDate appointmentDate);
    @Query(value = "SELECT * FROM Notification n WHERE n.appointment_date = :appointmentDate " +
            "AND EXTRACT(HOUR FROM n.appointment_time) = :hour " +
            "AND EXTRACT(MINUTE FROM n.appointment_time) = :minute", nativeQuery = true)
    List<Notification> findAllByAppointmentDateAndTime(
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("hour") int hour,
            @Param("minute") int minute);
}
