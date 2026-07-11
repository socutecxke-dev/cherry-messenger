package com.cherry.messenger.service;

import com.cherry.messenger.model.Chat;
import com.cherry.messenger.model.User;
import com.cherry.messenger.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat createChat(String name, User createdBy) {
        Chat chat = new Chat();
        chat.setName(name);
        chat.setCreatedBy(createdBy);
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
    }

    public List<Chat> getChatsForUser(User user) {
        // Пока возвращаем все чаты
        return chatRepository.findAll();
    }
}