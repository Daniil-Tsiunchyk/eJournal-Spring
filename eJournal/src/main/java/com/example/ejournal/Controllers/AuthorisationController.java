package com.example.ejournal.Controllers;

import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class AuthorisationController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            model.addAttribute("error", "Неверный адрес электронной почты или пароль");
            return "authorisation";
        }

        switch (user.getRole()) {
            case "STUDENT":
                return "redirect:/student-schedule?userId=" + user.getId();
            case "TEACHER":
                return "redirect:/teacher-schedule?userId=" + user.getId();
            case "DEAN":
                return "redirect:/tableschedule";
            default:
                model.addAttribute("error", "Неизвестная роль");
                return "authorisation";
        }
    }

    @GetMapping
    public String showAuthorisationPage() {
        return "authorisation";
    }
}