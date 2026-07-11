package com.cherry.messenger.config;

import com.cherry.messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("🔍 ИЩЕМ ПОЛЬЗОВАТЕЛЯ: " + username);
            return userRepository.findByUsername(username)
                    .map(user -> {
                        System.out.println("✅ ПОЛЬЗОВАТЕЛЬ НАЙДЕН: " + user.getUsername());
                        return org.springframework.security.core.userdetails.User
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .roles("USER")
                                .build();
                    })
                    .orElseThrow(() -> {
                        System.out.println("❌ ПОЛЬЗОВАТЕЛЬ НЕ НАЙДЕН: " + username);
                        return new UsernameNotFoundException("User not found");
                    });
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/chats", true)  // <- ЭТО ДОЛЖНО РАБОТАТЬ
                        .failureUrl("/login?error=true")    // <- ДОБАВИЛ
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());  // <- ВРЕМЕННО ОТКЛЮЧАЕМ CSRF

        return http.build();
    }
}