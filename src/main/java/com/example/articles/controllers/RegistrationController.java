package com.example.articles.controllers;

import com.example.articles.entities.User;
import com.example.articles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";  // Шаблон: src/main/resources/templates/registration.html
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user) {
        // Если email не указан, задаём его по умолчанию
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            user.setEmail(user.getUsername() + "@example.com");
        }
        // Задаём роль пользователя, дату создания и, при необходимости, другие поля
        user.setRole(User.Roles.USER_ROLE);
        user.setCreatedAt(LocalDateTime.now());
        if (user.getImageUrl() == null || user.getImageUrl().trim().isEmpty()) {
            user.setImageUrl("https://via.placeholder.com/150");
        }
        if (user.getBio() == null) {
            user.setBio("");
        }
        // Вызываем сервис для сохранения пользователя
        userService.createUser(user);
        return "redirect:/login";  // Перенаправление на страницу логина после успешной регистрации
    }
}
