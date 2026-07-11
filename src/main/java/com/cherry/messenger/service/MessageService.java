package com.cherry.messenger.service;

import com.cherry.messenger.model.Chat;
import com.cherry.messenger.model.Message;
import com.cherry.messenger.model.User;
import com.cherry.messenger.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message sendMessage(Chat chat, User sender, String content) {
        Message message = new Message();
        message.setChat(chat);      // ← Теперь должно работать!
        message.setSender(sender);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChat(Long chatId) {
        return messageRepository.findByChatIdOrderBySentAtAsc(chatId);
    }
}