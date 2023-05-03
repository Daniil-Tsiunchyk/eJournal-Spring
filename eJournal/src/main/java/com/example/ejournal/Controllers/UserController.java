package com.example.ejournal.Controllers;

import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.StudentGroupRepository;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @GetMapping("/newuser")
    public String showNewUserForm(Model model) {
        List<StudentGroup> groups = studentGroupRepository.findAll();
        model.addAttribute("groups", groups);
        return "newuser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam("email") String email,
                           @RequestParam("login") String login,
                           @RequestParam("group") Long groupId,
                           @RequestParam("role") String role,
                           RedirectAttributes redirectAttributes) {

        StudentGroup group = studentGroupRepository.findById(groupId).orElse(null);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setLogin(login);
        newUser.setPassword(login);
        newUser.setRole(role);
        newUser.setCreationDate(LocalDateTime.now());
        newUser.setStatus("Active");
        if (group != null) {
            newUser.setGroupNumber(group.getGroupNumber());
        }

        userRepository.save(newUser);
        redirectAttributes.addFlashAttribute("success", "User created with ID: " + newUser.getId());

        return "redirect:/newuser";
    }

}
