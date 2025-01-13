package com.example.doctor.repository;

import com.example.doctor.model.Appointment;
import com.example.doctor.model.enums.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean  existsByDoctorIdAndAppointmentDateAndAppointmentStartTime(
            long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentStartTime
    );

    boolean  existsByDoctorIdAndPatientIdAndAppointmentDate(
            long doctorId,
            long patientId,
            LocalDate appointmentDate
    );

    List<Appointment> findByDoctorIdAndAppointmentDateOrderByPatientIdAsc(
            long doctorId,
            LocalDate appointmentDate
    );

    List<Appointment> findByDoctorIdAndAppointmentDate(
            long doctorId,
            LocalDate appointmentDate
    );

    public List<Appointment> findAllByPatientIdAndAppointmentDateGreaterThanEqual(
            long patientId, LocalDate date);

    // Custom query to find distinct patient IDs for a specific doctor
    @Query("SELECT DISTINCT a.patientId FROM Appointment a WHERE a.doctorId = :doctorId")
    List<Long> findDistinctPatientIdsByDoctorId(@Param("doctorId") long doctorId);

    int countByDoctorIdAndAppointmentType(long doctorId, AppointmentType appointmentType);

    int countByDoctorId(long doctorId);

}
