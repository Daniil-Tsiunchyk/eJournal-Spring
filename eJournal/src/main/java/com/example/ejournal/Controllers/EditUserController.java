package com.example.ejournal.Controllers;

import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/editUser/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        model.addAttribute("user", user);
        return "edituser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("userId") Long userId,
                             @RequestParam("email") String email,
                             @RequestParam("login") String login,
                             @RequestParam("groupNumber") String groupNumber) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        user.setEmail(email);
        user.setLogin(login);
        user.setGroupNumber(groupNumber);
        userRepository.save(user);

        return "redirect:/tablestudents";
    }
}
