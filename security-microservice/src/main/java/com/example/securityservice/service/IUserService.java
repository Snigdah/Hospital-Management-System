package com.example.securityservice.service;

import com.example.securityservice.DTO.response.UserInformation;

public interface IUserService {
    public UserInformation getUserInformation(long userId);
}
