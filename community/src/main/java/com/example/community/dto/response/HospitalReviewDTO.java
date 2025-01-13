package com.example.community.dto.response;

import jakarta.persistence.Column;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalReviewDTO {
    private long userId;
    private String userName;
    private String imgUrl;
    private double ratting;
    private String reviewMessage;
}
