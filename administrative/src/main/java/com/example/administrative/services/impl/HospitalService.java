package com.example.administrative.services.impl;

import com.example.administrative.dto.request.DeleteDeptDto;
import com.example.administrative.dto.request.DepartmentDTO;
import com.example.administrative.dto.response.DepartmentDetailsDTO;
import com.example.administrative.exception.CustomeException;
import com.example.administrative.model.Department;
import com.example.administrative.model.Room;
import com.example.administrative.repository.DepartmentRepository;
import com.example.administrative.repository.RoomRepository;
import com.example.administrative.services.IHospitalService;
import com.example.administrative.utils.Constants;
import com.example.administrative.utils.EnumValidation;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HospitalService implements IHospitalService {
    private final DepartmentRepository deptRepository;
    private final RoomRepository roomRepository;

    public HospitalService(DepartmentRepository deptRepository, RoomRepository roomRepository) {
        this.deptRepository = deptRepository;
        this.roomRepository = roomRepository;
    }

    // Create Department
    @Transactional
    @Override
    public void createDepartmentWithRooms(DepartmentDTO departmentDTO){
        log.info("Attempting to create department: {}", departmentDTO.getDeptName());
        // Validate dept capacity and dept already exists or not
        validateDepartmentData(departmentDTO);
        checkIfDepartmentExists(departmentDTO.getDeptName());

        // Calculate and create room
        long startingRoomNumber = calculateStartingRoomNumber();
        Department department = buildDepartment(departmentDTO);
        EnumValidation.parseRoomType(departmentDTO.getRoomType());

        // Save Department & Room in database
        deptRepository.save(department);
        createAndAssignRooms(department, startingRoomNumber, departmentDTO.getCapacity(), departmentDTO.getRoomType());
    }

    // Fetch all dept with room details
    @Override
    public List<DepartmentDetailsDTO> getAllDepartmentsWithRoomDetails() {
        log.info("Fetching all departments with room details");
        List<Department> departments = deptRepository.findAll();

        return departments.stream().map(department -> {
            long availableRooms = department.getRooms().stream().filter(Room::getIsAvailable).count();
            long unavailableRooms = department.getRooms().size() - availableRooms;

            return DepartmentDetailsDTO.builder()
                    .deptId(department.getDeptId())
                    .deptName(department.getDeptName())
                    .capacity(department.getCapacity())
                    .availableRooms(availableRooms)
                    .unavailableRooms(unavailableRooms)
                    .build();
        }).collect(Collectors.toList());
    }

    // Delete department
    @Transactional
    public void deleteDepartment(DeleteDeptDto request) {
        // Check if the department exists & Ensure all rooms are available for deletion
        log.info("Attempting to delete department: {}", request.getDeptName());
        Department department = getExistingDepartment(request.getDeptName());
        ensureAllRoomsAreAvailable(department);

        // Proceed with the deletion of the department
        deptRepository.delete(department);
    }

    // Validate Department exists
    private void checkIfDepartmentExists(String deptName) {
        log.debug("Checking if department exists: {}", deptName);
        deptRepository.findByDeptName(deptName).ifPresent(dept -> {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Department with name '" + deptName + "' already exists.");
        });
    }

    // Validate Department Exists
    public Department getExistingDepartment(String deptName) {
        log.debug("Retrieving existing department: {}", deptName);
        return deptRepository.findByDeptName(deptName)
                .orElseThrow(() -> new CustomeException(HttpStatus.NOT_FOUND, "Department with name '" + deptName + "' does not exist."));
    }

    // Valid Department Data
    private void validateDepartmentData(DepartmentDTO departmentDTO) {
        log.debug("Validating department data for: {}", departmentDTO.getDeptName());
        if(departmentDTO.getCapacity() > Constants.MAX_ROOMS_PER_DEPARTMENT || departmentDTO.getCapacity() < 1){
            log.error("Invalid room capacity: {}", departmentDTO.getCapacity());
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Room capacity must be less then or equal 1000 and more then 0");
        }
    }

    // Calculate Start Room Capacity
    private long calculateStartingRoomNumber() {
        log.debug("Calculating starting room number");
        return roomRepository.findMaxRoomNumber().map(maxRoomNumber -> ((maxRoomNumber / 1000) + 1) * 1000)
                .orElse(Constants.STARTING_ROOM_NUMBER);
    }

    // Map DTO to Model class
    private Department buildDepartment(DepartmentDTO departmentDTO) {
        log.debug("Building department entity for: {}", departmentDTO.getDeptName());
        Department department = new Department();
        department.setDeptName(departmentDTO.getDeptName());
        department.setCapacity(departmentDTO.getCapacity());
        return department;
    }

    // Create Assigning rooms
    private void createAndAssignRooms(Department department, long startingRoomNumber, long capacity, String roomTypeStr) {
        log.debug("Creating and assigning rooms for department: {}", department.getDeptName());
        Set<Room> rooms = new HashSet<>();

        for (int i = 0; i < capacity; i++) {
            Room room = new Room();
            room.setRoomNumber(startingRoomNumber + i);
            room.setDepartment(department);
            room.setIsAvailable(true);
            room.setRoomType(EnumValidation.parseRoomType(roomTypeStr));
            rooms.add(room);
        }
        department.setRooms(rooms);
        roomRepository.saveAll(rooms);
    }

    // Validate room availability
    private void ensureAllRoomsAreAvailable(Department department) {
        log.info("Validating room availability for department: {}", department.getDeptName());
        boolean hasUnavailableRooms = department.getRooms().stream()
                .anyMatch(room -> !room.getIsAvailable());

        if (hasUnavailableRooms) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Department cannot be deleted as it has rooms that are not available.");
        }
        log.info("All rooms are available in department: {}", department.getDeptName());
    }

}
