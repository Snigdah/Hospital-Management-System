package com.example.doctor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private int totalPatient;
    private int totalAppointment;
    private int todayAppointment;
    private int totalOnlineAppointments;
}
