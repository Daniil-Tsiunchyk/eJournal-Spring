package com.example.ejournal.Controllers;

import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.StudentGroupRepository;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @GetMapping("/tablestudents")
    public String showStudentTable(Model model) {
        List<StudentGroup> groups = studentGroupRepository.findAll();
        model.addAttribute("groups", groups);
        return "tablestudents";
    }

    @GetMapping("/tablestudents/{groupNumber}")
    public String showStudentsByGroup(@PathVariable String groupNumber, Model model) {
        List<User> users = userRepository.findByGroupNumberAndRole(groupNumber, "USER");
        model.addAttribute("students", users);
        return "tablestudents";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
        userRepository.deleteById(userId);
        redirectAttributes.addFlashAttribute("success", "User deleted with ID: " + userId);
        return "redirect:/tablestudents";
    }
}
