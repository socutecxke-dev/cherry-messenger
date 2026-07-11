package com.cherry.messenger.controller;

import com.cherry.messenger.model.User;
import com.cherry.messenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        System.out.println("ПОЛНАЯ РЕГИСТРАЦИЯ: " + username);

        if (userService.existsByUsername(username)) {
            System.out.println("ПОЛЬЗОВАТЕЛЬ УЖЕ ЕСТЬ!");
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }

        System.out.println("СОХРАНЯЕМ ПОЛЬЗОВАТЕЛЯ...");
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userService.save(user);

        System.out.println("ПОЛЬЗОВАТЕЛЬ СОХРАНЕН!");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}