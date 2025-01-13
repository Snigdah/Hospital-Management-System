package com.example.doctor.services.impl;

import com.example.doctor.dto.request.AppointmentCreationDTO;
import com.example.doctor.dto.request.NotificationCreateDTO;
import com.example.doctor.dto.request.UserIdDTO;
import com.example.doctor.dto.response.AppointmentDoctorDTO;
import com.example.doctor.dto.response.AppointmentResponseDTO;
import com.example.doctor.dto.response.AvailableSlotResponseDTO;
import com.example.doctor.dto.response.UserInfoDTO;
import com.example.doctor.exception.CustomeException;
import com.example.doctor.exception.ResourceNotFoundException;
import com.example.doctor.feingClient.UserServiceClient;
import com.example.doctor.model.Appointment;
import com.example.doctor.model.Doctor;
import com.example.doctor.repository.AppointmentRepository;
import com.example.doctor.repository.DoctorRepository;
import com.example.doctor.services.IAppointmentService;
import com.example.doctor.utils.EnumValidation;
import com.example.doctor.webclient.IPushNotificationServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final IPushNotificationServiceClient pushNotificationServiceClient;
    private final UserServiceClient userServiceClient;
    private final DoctorService doctorService;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, IPushNotificationServiceClient pushNotificationServiceClient, UserServiceClient userServiceClient, DoctorService doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.pushNotificationServiceClient = pushNotificationServiceClient;
        this.userServiceClient = userServiceClient;
        this.doctorService = doctorService;
    }

    // Get Appointments By a Doctor for specified Date
    public List<AppointmentResponseDTO> getAppointmentsByDoctorAndDate(long doctorId, LocalDate date) {
        log.info("Fetching all appointment in date {}", date);

        // Retrieve the appointment List and user info
        List<Appointment> appointmentList = appointmentRepository.findByDoctorIdAndAppointmentDateOrderByPatientIdAsc(doctorId, date);
        List<UserInfoDTO> users = getUserInfoForAppointments(appointmentList);

        // Merge both information and return
        return mergeAppointmentAndUserInfo(appointmentList, users);
    }

    // Get Available slot for specific Doctor
    @Override
    public List<AvailableSlotResponseDTO> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findByUserId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "DoctorId", doctorId));
        LocalTime startTime = doctor.getStartTime();
        LocalTime endTime = doctor.getEndTime();

        List<AvailableSlotResponseDTO> slots = new ArrayList<>();

        while (!startTime.equals(endTime)) {
            LocalTime currentEndTime = startTime.plusMinutes(15);
            slots.add(new AvailableSlotResponseDTO(startTime, currentEndTime));
            startTime = currentEndTime;
        }

        List<AvailableSlotResponseDTO> occupiedSlots = appointmentRepository
                .findByDoctorIdAndAppointmentDate(doctorId, date)
                .stream()
                .map(appointment -> AvailableSlotResponseDTO
                        .builder()
                        .appointmentStartTime(appointment.getAppointmentStartTime())
                        .appointmentEndTime(appointment.getAppointmentEndTime())
                        .build())
                .toList();

        slots.removeAll(occupiedSlots);
        return slots;
    }

    // Fetch user info from Patient Microservice
    private List<UserInfoDTO> getUserInfoForAppointments(List<Appointment> appointments) {
        log.info("Getting user info for Appointments");
        List<UserIdDTO> userIdDTOs = extractUserIdsFromAppointments(appointments);
        return userServiceClient.getUserInfo(userIdDTOs);
    }

    // Extract user from appointments
    private List<UserIdDTO> extractUserIdsFromAppointments(List<Appointment> users) {
        log.info("Extracting user IDs from appointments");
        return users.stream()
                .map(Appointment::getPatientId)
                .distinct()
                .sorted()
                .map(UserIdDTO::new)
                .collect(Collectors.toList());
    }

    // Merge appointments and users information
    private List<AppointmentResponseDTO> mergeAppointmentAndUserInfo(List<Appointment> appointmentList,
                                                                     List<UserInfoDTO> users) {
        log.info("Merging appointment and user info");
        List<AppointmentResponseDTO> responseList = new ArrayList<>();

        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment appointment = appointmentList.get(i);
            UserInfoDTO user = users.get(i);

            if (user != null) {
                AppointmentResponseDTO dto = buildAppointmentResponseDTO(appointment, user);
                responseList.add(dto);
            } else {
                log.warn("No user info found for review with userId: {}", appointment.getPatientId());
            }
        }

        return responseList;
    }

    //Create Appointment
    @Override
    public void createAppointment(AppointmentCreationDTO appointmentCreationDTO, long patientId) {
        Doctor doctor = validateAndGetDoctor(appointmentCreationDTO);
        validateAppointmentDateAndTime(appointmentCreationDTO, patientId, doctor);
        validateTimeSlot(appointmentCreationDTO, doctor);

        Appointment newAppointment = createNewAppointment(appointmentCreationDTO, patientId);
        appointmentRepository.save(newAppointment);

        sendNotification(doctor, patientId, appointmentCreationDTO);
    }

    // Validate Doctor
    private Doctor validateAndGetDoctor(AppointmentCreationDTO appointmentCreationDTO) {
        return doctorRepository.findByUserId(appointmentCreationDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "DoctorId", appointmentCreationDTO.getDoctorId()));
    }

    // Validate Appointment Date and Time
    private void validateAppointmentDateAndTime(AppointmentCreationDTO appointmentCreationDTO, long patientId, Doctor doctor) {
        if (appointmentRepository.existsByDoctorIdAndPatientIdAndAppointmentDate(
                appointmentCreationDTO.getDoctorId(), patientId, appointmentCreationDTO.getAppointmentDate())) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "You can't create multiple appointments on the same date");
        }

        boolean slotOccupied = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentStartTime(
                doctor.getUserId(),
                appointmentCreationDTO.getAppointmentDate(),
                appointmentCreationDTO.getAppointmentStartTime());

        if (slotOccupied) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "This slot is already occupied, Try another slot");
        }
    }

    // Validate Time Slot
    private void validateTimeSlot(AppointmentCreationDTO appointmentCreationDTO, Doctor doctor) {
        int doctorStartMinutes = doctor.getStartTime().toSecondOfDay() / 60;
        int appointmentStartMinutes = appointmentCreationDTO.getAppointmentStartTime().toSecondOfDay() / 60;
        int appointmentEndMinutes = appointmentStartMinutes + 15;

        if (appointmentCreationDTO.getAppointmentStartTime().isBefore(doctor.getStartTime())
                || !appointmentCreationDTO.getAppointmentStartTime().isBefore(doctor.getEndTime())
                || (appointmentEndMinutes - doctorStartMinutes) % 15 != 0) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid Time Slot");
        }
    }

    // Map DTO to Entity
    private Appointment createNewAppointment(AppointmentCreationDTO appointmentCreationDTO, long patientId) {
        Appointment newAppointment = new Appointment();
        newAppointment.setPatientId(patientId);
        newAppointment.setDoctorId(appointmentCreationDTO.getDoctorId());
        newAppointment.setAppointmentDate(appointmentCreationDTO.getAppointmentDate());
        newAppointment.setAppointmentStartTime(appointmentCreationDTO.getAppointmentStartTime());
        newAppointment.setAppointmentEndTime(appointmentCreationDTO.getAppointmentStartTime().plusMinutes(15));
        newAppointment.setAppointmentType(EnumValidation.parseAppointmentType(appointmentCreationDTO.getAppointmentType()));
        return newAppointment;
    }

    // Send Notification
    private void sendNotification(Doctor doctor, long patientId, AppointmentCreationDTO appointmentCreationDTO) {
        NotificationCreateDTO notificationCreateDTO = new NotificationCreateDTO();
        notificationCreateDTO.setDoctorId(doctor.getUserId());
        notificationCreateDTO.setPatientId(patientId);
        notificationCreateDTO.setDoctorStartTime(doctor.getStartTime());
        notificationCreateDTO.setAppointmentDate(appointmentCreationDTO.getAppointmentDate());
        notificationCreateDTO.setAppointmentTime(appointmentCreationDTO.getAppointmentStartTime());

        // Send notification
        sendToNotificationMicroservice(notificationCreateDTO);
        log.info("Notification sent for appointment creation");
    }

    private NotificationCreateDTO createNotificationDTO(long patientId, AppointmentCreationDTO appointmentCreationDTO, Doctor doctor) {
        NotificationCreateDTO notificationCreateDTO = new NotificationCreateDTO();
        notificationCreateDTO.setDoctorId(doctor.getUserId());
        notificationCreateDTO.setPatientId(patientId);
        notificationCreateDTO.setDoctorStartTime(doctor.getStartTime());
        notificationCreateDTO.setAppointmentDate(appointmentCreationDTO.getAppointmentDate());
        notificationCreateDTO.setAppointmentTime(appointmentCreationDTO.getAppointmentStartTime());
        return notificationCreateDTO;
    }

    // Communicates with the administrative microservice for room availability ROLL BACK using REACTIVE Programing
    private void sendToNotificationMicroservice(NotificationCreateDTO createNotificationDTO) {
        pushNotificationServiceClient.saveNotification(createNotificationDTO)
                .subscribe(
                        response -> log.info("Data received successfully by Notification Microservice"),
                        ex -> log.error("Failed to Send data to Notification Microservice: " + ex.getMessage())
                );
    }

    // Fetch Upcoming Apportionment
    public List<AppointmentDoctorDTO> getPatientUpcomingAppointments(long patientId) {
        // Fetch list of appointment
        List<Appointment> appointmentList = appointmentRepository
                .findAllByPatientIdAndAppointmentDateGreaterThanEqual(patientId, LocalDate.now());

        List<AppointmentDoctorDTO> appointmentDoctorDTOS = new ArrayList<>();
        // Map with doctors names
        for (Appointment appointment : appointmentList) {
            appointmentDoctorDTOS.add(mapToUpcomingAppointment(
                    appointment, doctorService.findDoctorById(appointment.getDoctorId())));
        }
        return appointmentDoctorDTOS;
    }

    // Map Entity to DTO
    private AppointmentDoctorDTO mapToUpcomingAppointment(Appointment appointment, Doctor doctor) {
        return AppointmentDoctorDTO
                .builder()
                .doctorName(doctor.getDoctorName())
                .imageUrl(doctor.getImgUrl())
                .startTime(appointment.getAppointmentStartTime())
                .endTime(appointment.getAppointmentEndTime())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentType(appointment.getAppointmentType().toString())
                .build();
    }

    // Map Entity to DTO
    private AppointmentResponseDTO buildAppointmentResponseDTO(Appointment appointment, UserInfoDTO user) {
        return AppointmentResponseDTO.builder()
                .patientId(user.getUserId())
                .patientName(user.getUserName())
                .appointmentStartTime(appointment.getAppointmentStartTime())
                .appointmentEndTime(appointment.getAppointmentEndTime())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentType(appointment.getAppointmentType().toString())
                .imgUrl(user.getImgUrl())
                .build();
    }
}
