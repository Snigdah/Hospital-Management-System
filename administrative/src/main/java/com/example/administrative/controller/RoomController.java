package com.example.administrative.controller;

import com.example.administrative.dto.request.DeptDTO;
import com.example.administrative.dto.response.RoomDTO;
import com.example.administrative.response.ResponseHandler;
import com.example.administrative.services.IRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administrative/rooms")
public class RoomController {

    private final IRoomService roomService;

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllRooms(){
        List<RoomDTO> response = roomService.getAllRooms();
        return ResponseHandler.generateResponse("Fetch All Rooms Successfully", HttpStatus.OK, response);
    }

    @GetMapping("/getByDepartment")
    public ResponseEntity<Object> getRoomsByDepartment(@RequestBody DeptDTO department){
        List<RoomDTO> response = roomService.getAllRoomsByDepartmentName(department);
        return ResponseHandler.generateResponse("Fetch All Rooms Successfully", HttpStatus.OK, response);
    }
}
