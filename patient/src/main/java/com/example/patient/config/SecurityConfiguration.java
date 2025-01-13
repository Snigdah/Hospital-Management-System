package com.example.patient.config;


import com.example.patient.security.JwtAuthenticationFilter;
import com.example.patient.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/patient/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/patient/health-info/create").hasRole(Constants.ROLE_PATIENT)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/patient/health-info/update").hasRole(Constants.ROLE_PATIENT)

                        .requestMatchers(HttpMethod.GET, "/api/v1/patient/profile/get/{userId}").hasAnyRole(Constants.ROLE_PATIENT, Constants.ROLE_ADMINISTRATIVE)
                        .requestMatchers(HttpMethod.GET, "/api/v1/patient/profile/getAll").hasRole( Constants.ROLE_ADMINISTRATIVE)
                        .requestMatchers(HttpMethod.GET, "/api/v1/patient/profile/health-info").hasRole(Constants.ROLE_PATIENT)
                        .requestMatchers(HttpMethod.GET, "/api/v1/patient/profile/search").hasRole(Constants.ROLE_PATIENT)

                        .requestMatchers( "/api/v1/patient/proxy/**").permitAll()

                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        ;

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Cache-Control"
        ));
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}