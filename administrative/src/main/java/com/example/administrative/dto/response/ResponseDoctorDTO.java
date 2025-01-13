package com.example.administrative.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDoctorDTO {
    private long roomId;
    private long roomNumber;
    private long deptId;
}
