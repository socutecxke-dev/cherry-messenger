package com.cherry.messenger.config;

import com.cherry.messenger.model.User;
import com.cherry.messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("🔧 DataLoader запущен...");
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123"));
            userRepository.save(admin);
            System.out.println("✅ АДМИН СОЗДАН: admin / 123");
        } else {
            System.out.println("✅ АДМИН УЖЕ СУЩЕСТВУЕТ");
        }
    }
}