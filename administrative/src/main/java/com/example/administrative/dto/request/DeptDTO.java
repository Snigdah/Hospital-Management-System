package com.example.administrative.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeptDTO {
    @NotEmpty(message = "Department should not be empty")
    private String deptName;
}
