package com.example.securityservice.service.impl;

import com.example.securityservice.DTO.response.AuthenticationResponseDTO;
import com.example.securityservice.DTO.request.LogInRequestDTO;
import com.example.securityservice.DTO.response.LogInResponseDTO;
import com.example.securityservice.DTO.request.RegisterRequestDTO;
import com.example.securityservice.entity.Role;
import com.example.securityservice.entity.User;
import com.example.securityservice.exception.AuthenticationException;
import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.security.JwtService;
import com.example.securityservice.service.IAuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        // Check if the email already exists in the database
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthenticationException(HttpStatus.BAD_REQUEST, "Email address is already taken.");
        }

        Role userRole;
        if ("PATIENT".equalsIgnoreCase(request.getRole())) {
            userRole = Role.PATIENT;
        } else if ("DOCTOR".equalsIgnoreCase(request.getRole())) {
            userRole = Role.DOCTOR;
        } else if ("ADMINISTRATIVE".equalsIgnoreCase(request.getRole())) {
            userRole = Role.ADMINISTRATIVE;
        } else {
            throw new AuthenticationException(HttpStatus.BAD_REQUEST, "Invalid role. Supported roles are PATIENT, DOCTOR and ADMINISTRATIVE.");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        // Save the user to the database
        User newUser = userRepository.save(user);

        // Generate JWT token and return the response
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponseDTO.builder()
                .userId(newUser.getId())
                .role(user.getRole().toString())
                .token(jwtToken)
                .build();
    }

    public LogInResponseDTO authenticate(LogInRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            // Check is user exists or not
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new AuthenticationException(HttpStatus.NOT_FOUND, "User not found"));

            var jwtToken = jwtService.generateToken(user);
            return LogInResponseDTO.builder()
                    .userId(user.getId())
                    .role(user.getRole().toString())
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            // Handle invalid credentials (wrong email or password)
            throw new AuthenticationException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        } catch (ExpiredJwtException e) {
            // Handle expired JWT tokens
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Session has expired. Please log in again");
        }  catch (Exception e) {
            // Handle other exceptions here
            throw new AuthenticationException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed");
        }
    }

}
