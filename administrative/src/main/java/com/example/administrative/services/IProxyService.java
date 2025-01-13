package com.example.administrative.services;

import com.example.administrative.dto.request.CreateDoctorDTO;
import com.example.administrative.dto.request.RoomAvailableDTO;
import com.example.administrative.dto.response.ResponseDoctorDTO;
import com.example.administrative.dto.response.RoomInfoDTO;

public interface IProxyService {
    public ResponseDoctorDTO assignRoomToDoctor(CreateDoctorDTO createDoctorDTO);
    public RoomInfoDTO getRoomInfo(long roomId);
    public void changeRoomAvailability(RoomAvailableDTO roomAvailableDTO);
}
