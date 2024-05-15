package com.kindnesskattle.bddAtcProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedOrigins("http://127.0.0.1:5501") // Allow requests from your frontend domain
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow only specific methods
                .allowCredentials(true); // Allow credentials (e.g., cookies)
    }
}
