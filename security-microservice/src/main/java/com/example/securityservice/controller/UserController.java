package com.example.securityservice.controller;

import com.example.securityservice.DTO.response.UserInformation;
import com.example.securityservice.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/user")
@AllArgsConstructor
public class UserController {

    private final IUserService userService;
    @GetMapping("/user-information/{userId}")
    public UserInformation getUserInformation(@PathVariable long userId) {
        return userService.getUserInformation(userId);
    }
}
