package com.example.administrative.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    @NotEmpty(message = "Department should not be empty")
    private String deptName;

    @NotNull
    @Min(value = 0, message = "Capacity should not be negative or empty or null")
    private long capacity;

    @NotEmpty(message = "Room type should not be empty")
    private String roomType;
}
