package com.example.community.feingClient;

import com.example.community.security.CustomWebAuthenticationDetails;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getDetails() instanceof CustomWebAuthenticationDetails) {
                    CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
                    template.header("Authorization", "Bearer " + details.getJwtToken());
                }
            }
        };
    }
}
