package com.example.administrative.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDetailsDTO {
    private long deptId;
    private String deptName;
    private long capacity;
    private long availableRooms;
    private long unavailableRooms;
}
