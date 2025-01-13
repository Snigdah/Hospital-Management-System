package com.example.administrative.services;

import com.example.administrative.dto.request.DeptDTO;
import com.example.administrative.dto.response.RoomDTO;

import java.util.List;

public interface IRoomService {
    public List<RoomDTO> getAllRooms();
    public List<RoomDTO> getAllRoomsByDepartmentName(DeptDTO deptDTO);
}
