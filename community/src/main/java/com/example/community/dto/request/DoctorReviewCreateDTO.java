package com.example.community.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorReviewCreateDTO {
    @NotNull(message = "Doctor id can't be null or empty")
    private long doctorId;

    @NotEmpty(message = "Review Message should not be null or empty")
    private String reviewMessage;
}
