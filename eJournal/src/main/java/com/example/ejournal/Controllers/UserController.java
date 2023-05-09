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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/tablestudents")
    public String showTableStudents(@RequestParam(required = false) String groupNumber, Model model) {
        List<User> users;
        if (groupNumber == null || groupNumber.isEmpty()) {
            users = userRepository.findAllByRole("STUDENT");
        } else {
            users = userRepository.findAllByRoleAndGroupNumber("STUDENT", groupNumber);
        }
        List<StudentGroup> groups = studentGroupRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("groups", groups);
        return "tablestudents";
    }

    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "edit-user";
        } else {
            return "redirect:/tablestudents";
        }
    }

    @PostMapping("/edit-user/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User updatedUser) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userToUpdate = user.get();
            userToUpdate.setEmail(updatedUser.getEmail());
            userToUpdate.setLogin(updatedUser.getLogin());
            userToUpdate.setRole(updatedUser.getRole());
            userToUpdate.setCreationDate(updatedUser.getCreationDate());
            userToUpdate.setStatus(updatedUser.getStatus());
            userToUpdate.setGroupNumber(updatedUser.getGroupNumber());
            userToUpdate.setName(updatedUser.getName());
            userToUpdate.setSurname(updatedUser.getSurname());
            userToUpdate.setSubject(updatedUser.getSubject());
            userRepository.save(userToUpdate);
        }
        return "redirect:/tablestudents";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
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
        List<String> times = Arrays.asList("9:00-10:20", "10:35-11:55", "12:25-13:45", "14:00-15:20", "15:50-17:10");
        model.addAttribute("days", days);
        model.addAttribute("times", times);
        model.addAttribute("studentSchedules", studentSchedules);
        model.addAttribute("userId", userId);
        return "student-schedule";
    }
}
