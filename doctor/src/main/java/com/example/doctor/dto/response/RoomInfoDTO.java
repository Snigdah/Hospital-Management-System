package com.example.doctor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoDTO {
    private long roomNumber;
    private String deptName;
    private String roomType;
}