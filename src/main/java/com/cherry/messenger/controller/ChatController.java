package com.cherry.messenger.controller;

import com.cherry.messenger.model.Chat;
import com.cherry.messenger.model.Message;
import com.cherry.messenger.model.User;
import com.cherry.messenger.service.ChatService;
import com.cherry.messenger.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.cherry.messenger.service.UserService;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/chats")
    public String showChats(Model model, Authentication auth) {
        // Пока просто показываем страницу без ошибок
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("username", userDetails.getUsername());
        return "chats";
    }

    // Пока заглушка для просмотра конкретного чата
    @GetMapping("/chats/{chatId}")
    public String showChat(@PathVariable Long chatId, Model model) {
        try {
            Chat chat = chatService.getChatById(chatId);
            model.addAttribute("chat", chat);
            return "chat";
        } catch (RuntimeException e) {
            System.out.println("❌ ЧАТ НЕ НАЙДЕН: " + chatId);
            return "redirect:/chats";  // ← Возвращаем на список чатов
        }
    }

    @PostMapping("/chats/{chatId}/send")
    public String sendMessage(
            @PathVariable Long chatId,
            @RequestParam String content,
            Authentication auth
    ) {
        try {
            // Получаем имя пользователя из Spring Security
            String username = auth.getName();
            System.out.println("🔍 ОТПРАВКА СООБЩЕНИЯ ОТ: " + username);

            // Ищем пользователя в БД
            User currentUser = userService.findByUsername(username);
            if (currentUser == null) {
                System.out.println("❌ ПОЛЬЗОВАТЕЛЬ НЕ НАЙДЕН: " + username);
                return "redirect:/login";
            }

            // Ищем чат
            Chat chat = chatService.getChatById(chatId);
            if (chat == null) {
                System.out.println("❌ ЧАТ НЕ НАЙДЕН: " + chatId);
                return "redirect:/chats";
            }

            // Отправляем сообщение
            messageService.sendMessage(chat, currentUser, content);
            System.out.println("✅ СООБЩЕНИЕ ОТПРАВЛЕНО!");

        } catch (Exception e) {
            System.out.println("❌ ОШИБКА: " + e.getMessage());
            e.printStackTrace();  // <- ЭТО ВЫВЕДЕТ ОШИБКУ В КОНСОЛЬ
        }

        return "redirect:/chats/" + chatId;
    }
}