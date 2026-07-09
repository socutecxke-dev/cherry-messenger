package com.cherry.messenger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // РАЗРЕШАЕМ ВСЁ БЕЗ ВХОДА
                )
                .csrf(csrf -> csrf.disable());  // Отключаем защиту от CSRF (временно)
        return http.build();
    }
}