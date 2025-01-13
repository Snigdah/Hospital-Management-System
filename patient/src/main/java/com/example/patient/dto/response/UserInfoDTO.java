package com.example.patient.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private long userId;
    private String userName;
    private String imgUrl;
}
