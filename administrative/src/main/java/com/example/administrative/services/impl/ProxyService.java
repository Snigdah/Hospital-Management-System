package com.example.administrative.services.impl;

import com.example.administrative.dto.request.CreateDoctorDTO;
import com.example.administrative.dto.request.RoomAvailableDTO;
import com.example.administrative.dto.response.ResponseDoctorDTO;
import com.example.administrative.dto.response.RoomInfoDTO;
import com.example.administrative.model.Department;
import com.example.administrative.model.Room;
import com.example.administrative.repository.RoomRepository;
import com.example.administrative.services.IProxyService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProxyService implements IProxyService {
    private final HospitalService hospitalService;
    private final RoomService roomService;
    private final RoomRepository roomRepository;

    public ProxyService(HospitalService hospitalService, RoomService roomService, RoomRepository roomRepository) {
        this.hospitalService = hospitalService;
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    @Transactional
    @Override
    public ResponseDoctorDTO assignRoomToDoctor(CreateDoctorDTO createDoctorDTO) {
        log.info("Assigning room to doctor in department: {}", createDoctorDTO.getDeptName());

        // Check Department and that department room availability
        Department department = hospitalService.getExistingDepartment(createDoctorDTO.getDeptName());
        Room availableRoom = roomService.findAvailableRoomInDepartment(department);
        // Change in room availability in DB
        roomService.setRoomUnavailable(availableRoom);
        log.info("Room assigned to doctor: Room ID {}, Room Number {}", availableRoom.getRoomId(), availableRoom.getRoomNumber());

        return ResponseDoctorDTO.builder()
                .roomId(availableRoom.getRoomId())
                .roomNumber(availableRoom.getRoomNumber())
                .deptId(department.getDeptId())
                .build();
    }

    //Get Room Information
    @Override
    public RoomInfoDTO getRoomInfo(long roomId) {
        // Validate and fetch room information
        Room room = roomService.getRoomDetails(roomId);

        log.info("Fetch room info successfully");
        return RoomInfoDTO
                .builder()
                .roomNumber(room.getRoomNumber())
                .deptName(room.getDepartment().getDeptName())
                .roomType(room.getRoomType().toString())
                .build();
    }

    @Override
    public void changeRoomAvailability(RoomAvailableDTO roomAvailableDTO){
        // Validate and fetch room information
        Room room = roomService.getRoomDetails(roomAvailableDTO.getRoomId());
        log.info("Fetch room info successfully");

        // Change Availability status and save ini DB
        room.setIsAvailable(true);
        roomRepository.save(room);
    }
}
