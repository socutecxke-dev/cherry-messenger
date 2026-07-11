package com.cherry.messenger.config;

import com.cherry.messenger.model.Chat;
import com.cherry.messenger.model.User;
import com.cherry.messenger.repository.ChatRepository;
import com.cherry.messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("🔧 DataLoader запущен...");

        // 1. Создаем админа
        User admin = userRepository.findByUsername("admin")
                .orElseGet(() -> {
                    User newAdmin = new User();
                    newAdmin.setUsername("admin");
                    newAdmin.setPassword(passwordEncoder.encode("123"));
                    return userRepository.save(newAdmin);
                });
        System.out.println("✅ АДМИН ГОТОВ: admin / 123");

        // 2. Проверяем, есть ли чаты
        long chatCount = chatRepository.count();
        System.out.println("📊 КОЛИЧЕСТВО ЧАТОВ: " + chatCount);

        if (chatCount == 0) {
            Chat chat = new Chat();
            chat.setName("Общий чат");
            chat.setCreatedBy(admin);
            chat.setCreatedAt(LocalDateTime.now());
            chatRepository.save(chat);
            System.out.println("✅ СОЗДАН ТЕСТОВЫЙ ЧАТ: Общий чат (ID = " + chat.getId() + ")");
        } else {
            System.out.println("✅ ЧАТЫ УЖЕ СУЩЕСТВУЮТ");
            // Выводим все чаты в консоль
            chatRepository.findAll().forEach(c ->
                    System.out.println("   ЧАТ: ID=" + c.getId() + ", NAME=" + c.getName())
            );
        }
    }
}