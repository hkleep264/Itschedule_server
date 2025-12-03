package com.itschedule.itschedule_server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //Real 에서는 주석 처리 해야함
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/schedule/**")
                .allowedOrigins("http://localhost:5173")   // 프론트 origin 딱 1개만!
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);                    // 쿠키/세션 허용
    }
}

