package com.example.administrative.services;

import com.example.administrative.dto.request.DeleteDeptDto;
import com.example.administrative.dto.request.DepartmentDTO;
import com.example.administrative.dto.response.DepartmentDetailsDTO;

import java.util.List;

public interface IHospitalService {
    public void createDepartmentWithRooms(DepartmentDTO departmentDTO);
    public void deleteDepartment(DeleteDeptDto request);
    public List<DepartmentDetailsDTO> getAllDepartmentsWithRoomDetails();
}
