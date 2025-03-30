package com.example.articles.controllers;

import com.example.articles.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";  // Шаблон src/main/resources/templates/registration.html
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user) {
        // Логика для регистрации пользователя
        return "redirect:/login";  // После регистрации редиректим на страницу логина
    }
}
