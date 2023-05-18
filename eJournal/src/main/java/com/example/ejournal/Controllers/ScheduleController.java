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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private UserRepository userRepository;

    private List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
    private List<String> times = Arrays.asList("9:00-10:20", "10:35-11:55", "12:25-13:45", "14:00-15:20", "15:50-17:10");

    @GetMapping({"/newschedule", "/editschedule/{id}"})
    public String handleSchedule(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + id));
            model.addAttribute("schedule", schedule);
        }

        List<StudentGroup> studentGroups = studentGroupRepository.findAll();
        model.addAttribute("studentGroups", studentGroups);
        model.addAttribute("days", days);
        model.addAttribute("times", times);
        return id == null ? "newschedule" : "editschedule";
    }

    @PostMapping({"/addschedule", "/editschedule/{id}"})
    public String saveOrUpdateSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
        return "redirect:/schedule";
    }

    @GetMapping("/deleteschedule/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + id));
        scheduleRepository.delete(schedule);
        return "redirect:/schedule";
    }

    @GetMapping({"/teacher-schedule", "/student-schedule"})
    public String getUserSchedule(@RequestParam int userId, @PathVariable String action, Model model) {
        Optional<User> userOpt = userRepository.findById((long) userId);
        if (!userOpt.isPresent()) {
            model.addAttribute("error", "Пользователь с id " + userId + " не найден");
            return "authorisation";
        }

        User user = userOpt.get();
        List<Schedule> userSchedules;

        if ("teacher-schedule".equals(action)) {
            userSchedules = scheduleRepository.findByTeacherName(user.getName() + " " + user.getSurname());
        } else {
            userSchedules = scheduleRepository.findByGroupNumber(user.getGroupNumber());
        }

        model.addAttribute("userId", userId);
        model.addAttribute("userSchedules", userSchedules);
        model.addAttribute("days", days);
        model.addAttribute("times", times);
        return action;
    }

    @GetMapping({"/tablestudents", "/schedule"})
    public String showTableOrSchedule(@RequestParam(required = false) String groupNumber, @RequestParam(required = false) String role, Model model) {
        List<User> users;
        List<Schedule> schedules;

        if ("tablestudents".equals(role)) {
            users = getUserList(role, groupNumber);
            model.addAttribute("users", users);
        } else {
            schedules = getScheduleList(groupNumber);
            model.addAttribute("schedules", schedules);
            model.addAttribute("days", days);
            model.addAttribute("times", times);
        }

        List<StudentGroup> groups = studentGroupRepository.findAll();
        model.addAttribute("groups", groups);
        return role;
    }

    private List<User> getUserList(String role, String groupNumber) {
        if ((groupNumber == null || groupNumber.isEmpty()) && (role == null || role.isEmpty())) {
            return (List<User>) userRepository.findAll();
        } else if (role == null || role.isEmpty()) {
            return userRepository.findAllByGroupNumber(groupNumber);
        } else if (groupNumber == null || groupNumber.isEmpty()) {
            return userRepository.findAllByRole(role);
        } else {
            return userRepository.findAllByRoleAndGroupNumber(role, groupNumber);
        }
    }

    private List<Schedule> getScheduleList(String groupNumber) {
        if (groupNumber == null || groupNumber.isEmpty()) {
            return scheduleRepository.findAll();
        } else {
            return scheduleRepository.findAllByGroupNumber(groupNumber);
        }
    }
}
