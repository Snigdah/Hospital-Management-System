package com.example.community.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorReviewDTO {
    private long userId;
    private String userName;
    private String imgUrl;
    private String reviewMessage;
}
