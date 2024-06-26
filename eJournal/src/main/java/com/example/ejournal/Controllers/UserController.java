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

    private final UserRepository userRepository;

    private final StudentGroupRepository studentGroupRepository;

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public UserController(UserRepository userRepository, StudentGroupRepository studentGroupRepository, ScheduleRepository scheduleRepository) {
        this.userRepository = userRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        List<StudentGroup> groups = studentGroupRepository.findAll();
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("groups", groups);
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
            userToUpdate.setPassword(updatedUser.getPassword());
            userToUpdate.setRole(updatedUser.getRole());
//            userToUpdate.setCreationDate(updatedUser.getCreationDate());
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
                           @RequestParam("name") String name,
                           @RequestParam("surname") String surname,
                           RedirectAttributes redirectAttributes) {

        StudentGroup group = studentGroupRepository.findById(groupId).orElse(null);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setLogin(login);
        newUser.setPassword(login);
        newUser.setRole(role);
        newUser.setCreationDate(LocalDateTime.now());
        newUser.setStatus("Active");
        newUser.setName(name);
        newUser.setSurname(surname);
        if (group != null) {
            newUser.setGroupNumber(group.getGroupNumber());
        }

        userRepository.save(newUser);
        redirectAttributes.addFlashAttribute("success", "Пользователь создан с ID: " + newUser.getId());
        redirectAttributes.addFlashAttribute("user_info", newUser);
        return "redirect:/newuser";
    }


    @GetMapping("/student-schedule")
    public String getStudentSchedule(@RequestParam("userId") int userId, Model model) {
        User student = userRepository.findById((long) userId).orElse(null);
        if (student == null) {
            return "redirect:/authorisation";
        }

        List<Schedule> groupSchedules = scheduleRepository.findByGroupNumber(student.getGroupNumber());

        List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
        List<String> times = Arrays.asList("09:00", "10:35", "12:25", "14:00", "15:50");
        model.addAttribute("days", days);
        model.addAttribute("times", times);
        model.addAttribute("groupSchedules", groupSchedules);
        model.addAttribute("userId", userId);
        System.out.println("Schedules for group " + student.getGroupNumber() + ": " + groupSchedules);
        return "student-schedule";
    }

    @GetMapping("/tablestudents")
    public String showTableStudents(@RequestParam(required = false) String groupNumber,
                                    @RequestParam(required = false) String role,
                                    Model model) {
        List<User> users;
        if ((groupNumber == null || groupNumber.isEmpty()) && (role == null || role.isEmpty())) {
            users = (List<User>) userRepository.findAll();
        } else if (role == null || role.isEmpty()) {
            users = userRepository.findAllByGroupNumber(groupNumber);
        } else if (groupNumber == null || groupNumber.isEmpty()) {
            users = userRepository.findAllByRole(role);
        } else {
            users = userRepository.findAllByRoleAndGroupNumber(role, groupNumber);
        }

        List<StudentGroup> groups = studentGroupRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("groups", groups);
        return "tablestudents";
    }

    @GetMapping("/editUser/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        model.addAttribute("user", user);
        return "edit-user";
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
