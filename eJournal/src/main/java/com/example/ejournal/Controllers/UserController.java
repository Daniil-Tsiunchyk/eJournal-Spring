package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Schedule;
import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.ScheduleRepository;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/tablestudents")
    public String showStudentTable(Model model) {
        List<StudentGroup> groups = studentGroupRepository.findAll();
        List<User> users = userRepository.findByRole("USER");
        model.addAttribute("groups", groups);
        model.addAttribute("users", users);
        return "tablestudents";
    }


    @GetMapping("/tablestudents/{groupNumber}")
    public String showStudentsByGroup(@PathVariable String groupNumber, Model model) {
        List<User> users = userRepository.findByGroupNumberAndRole(groupNumber, "USER");
        model.addAttribute("users", users);
        return "tablestudents";
    }


    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
        userRepository.deleteById(userId);
        redirectAttributes.addFlashAttribute("success", "User deleted with ID: " + userId);
        return "redirect:/tablestudents";
    }

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
        redirectAttributes.addFlashAttribute("success", "Пользователь создан с ID: " + newUser.getId());

        return "redirect:/newuser";
    }

    @GetMapping("/student-schedule")
    public String getStudentSchedule(@RequestParam("userId") int userId, Model model) {
        User student = userRepository.findById((long) userId).orElse(null);
        if (student == null) {
        }

        List<Schedule> studentSchedules = scheduleRepository.findByGroupNumber(student.getGroupNumber());

        List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
        List<String> times = Arrays.asList("9:00-10:30", "10:45-12:15", "12:30-14:00", "14:15-15:45", "16:00-17:30");
        model.addAttribute("days", days);
        model.addAttribute("times", times);
        model.addAttribute("studentSchedules", studentSchedules);
        model.addAttribute("userId", userId);
        return "student-schedule";
    }
}
