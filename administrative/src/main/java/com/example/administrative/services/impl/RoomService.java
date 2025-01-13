package com.example.administrative.services.impl;

import com.example.administrative.dto.request.DeptDTO;
import com.example.administrative.dto.response.RoomDTO;
import com.example.administrative.exception.CustomeException;
import com.example.administrative.exception.ResourceNotFoundException;
import com.example.administrative.model.Department;
import com.example.administrative.model.Room;
import com.example.administrative.repository.DepartmentRepository;
import com.example.administrative.repository.RoomRepository;
import com.example.administrative.services.IRoomService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final DepartmentRepository deptRepository;

    public RoomService(RoomRepository roomRepository, DepartmentRepository deptRepository) {
        this.roomRepository = roomRepository;
        this.deptRepository = deptRepository;
    }

    // Get all rooms by ascending order room number
    @Override
    public List<RoomDTO> getAllRooms(){
        log.info("Fetching all rooms in ascending order of room number");
        List<Room> rooms = roomRepository.findAllOrderByRoomNumberAsc();
        return rooms.stream().map(this:: convertToRoomDto).collect(Collectors.toList());
    }

    //Get all rooms by Department Name
    @Override
    public List<RoomDTO> getAllRoomsByDepartmentName(DeptDTO deptDTO){
        // Optional check to ensure the department exists
        log.info("Fetching all rooms for department: {}", deptDTO.getDeptName());
        Department department = deptRepository.findByDeptName(deptDTO.getDeptName())
                .orElseThrow(() -> new CustomeException(HttpStatus.BAD_REQUEST, "Department not found with name: " + deptDTO.getDeptName()));

        // Fetch room by specific Dept
        List<Room> rooms = roomRepository.findAllByDepartmentNameOrderByRoomNumberAsc(department.getDeptName());
        return rooms.stream().map(this::convertToRoomDto).collect(Collectors.toList());
    }

    // Convert Model to DTO object
    private RoomDTO convertToRoomDto(Room room) {

        return RoomDTO
                .builder()
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType().toString())
                .isAvailable(room.getIsAvailable())
                .departmentName(room.getDepartment().getDeptName())
                .build();
    }

    public Room findAvailableRoomInDepartment(Department department) {
        return department.getRooms().stream()
                .filter(Room::getIsAvailable)
                .findFirst()
                .orElseThrow(() -> new CustomeException(HttpStatus.BAD_REQUEST, "No available rooms in department: " + department.getDeptName()));
    }

    @Transactional
    public void setRoomUnavailable(Room room) {
        room.setIsAvailable(false);
        roomRepository.save(room);
    }

    public Room getRoomDetails(long roomId){
        log.info("Check if room exists or not : {}", roomId);
        return roomRepository.findByRoomId(roomId)
                .orElseThrow(() ->  new ResourceNotFoundException("Room", "roomId", roomId));
    }
}
