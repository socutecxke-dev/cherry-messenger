package com.cherry.messenger.controller;

import com.cherry.messenger.model.Chat;
import com.cherry.messenger.model.User;
import com.cherry.messenger.service.ChatService;
import com.cherry.messenger.service.MessageService;
import com.cherry.messenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/chats")
    public String showChats(Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User currentUser = userService.findByUsername(userDetails.getUsername());

        // Получаем все чаты, где участвует пользователь
        List<Chat> chats = chatService.getChatsForUser(currentUser);

        model.addAttribute("chats", chats);
        model.addAttribute("username", currentUser.getUsername());
        return "chats";
    }

    @GetMapping("/chats/{chatId}")
    public String showChat(@PathVariable Long chatId, Model model, Authentication auth) {
        try {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User currentUser = userService.findByUsername(userDetails.getUsername());

            Chat chat = chatService.getChatById(chatId);
            var messages = messageService.getMessagesByChat(chatId);

            model.addAttribute("chat", chat);
            model.addAttribute("messages", messages);
            model.addAttribute("currentUser", currentUser);
            return "chat";
        } catch (RuntimeException e) {
            return "redirect:/chats";
        }
    }

    @PostMapping("/chats/create")
    public String createChat(@RequestParam String name, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User currentUser = userService.findByUsername(userDetails.getUsername());
        chatService.createChat(name, currentUser);
        return "redirect:/chats";
    }

    @PostMapping("/chats/{chatId}/send")
    public String sendMessage(
            @PathVariable Long chatId,
            @RequestParam String content,
            Authentication auth
    ) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User currentUser = userService.findByUsername(userDetails.getUsername());
        Chat chat = chatService.getChatById(chatId);
        messageService.sendMessage(chat, currentUser, content);
        return "redirect:/chats/" + chatId;
    }
}