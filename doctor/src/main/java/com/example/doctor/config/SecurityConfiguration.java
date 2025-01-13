package com.example.doctor.config;

import com.example.doctor.security.JwtAuthenticationFilter;
import com.example.doctor.utils.Constants;
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
                        .requestMatchers("/api/v1/doctor/auth/**").permitAll()
                        .requestMatchers("/api/v2/doctor/proxy/**").permitAll()


                        .requestMatchers(HttpMethod.GET, "/api/v1/doctor/info/getAll").hasRole(Constants.ROLE_ADMINISTRATIVE)
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctor/info/department/{deptId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctor/info/{doctorId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctor/info/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctor/info/changeStatus").hasRole(Constants.ROLE_DOCTOR)

                        .requestMatchers( HttpMethod.POST,"/api/v1/doctor/appointment/create").hasRole(Constants.ROLE_PATIENT)
                        .requestMatchers( HttpMethod.GET,"/api/v1/doctor/appointment/getAll").hasRole(Constants.ROLE_DOCTOR)
                        .requestMatchers( HttpMethod.GET,"/api/v1/doctor/appointment/get/availableSlots/**").permitAll()
                        .requestMatchers( HttpMethod.GET,"/api/v1/doctor/appointment/upcoming").hasRole(Constants.ROLE_PATIENT)

                        .requestMatchers( "/api/v1/doctor/dashboard/**").hasRole(Constants.ROLE_DOCTOR)

                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

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
