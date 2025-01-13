package com.example.administrative.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailableDTO {
    @NotNull
    @Min(value = 0, message = "RoomId should not be negative or empty or null")
    private long roomId;
}
