package com.example.doctor.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDoctorDTO {
    private long roomId;
    private long roomNumber;
    private long deptId;
}
