package com.example.securityservice.service.impl;

import com.example.securityservice.DTO.response.UserInformation;
import com.example.securityservice.entity.User;
import com.example.securityservice.exception.ResourceNotFoundException;
import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInformation getUserInformation(long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User", "id", userId));

        UserInformation userInformation = new UserInformation();
        userInformation.setUsername(user.getUsername());
        userInformation.setEmail(user.getEmail());

        return userInformation;
    }
}
