package com.vitamin_market.vitamin_compare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tüm endpoint'ler için geçerli
                .allowedOrigins("https://vitamin-compare-web.vercel.app/") // Frontend'in adresini ekle
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Kullanılacak HTTP metotları
                .allowedHeaders("*") // Tüm header'lara izin ver
                .allowCredentials(true); // Eğer oturum yönetimi (cookie, token) kullanıyorsan true olmalı
    }
}