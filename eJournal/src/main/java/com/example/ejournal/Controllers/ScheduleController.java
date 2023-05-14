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

    @GetMapping
    public String showSchedule(Model model) {
        List<Schedule> schedules = scheduleRepository.findAll();
        model.addAttribute("schedules", schedules);
        List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
        List<String> times = Arrays.asList("09:00", "10:35", "12:25", "14:00", "15:50", "17:25");
        model.addAttribute("days", days);
        model.addAttribute("times", times);
        return "tableschedule";
    }


    @GetMapping("/newschedule")
    public String addSchedule(Model model) {
        List<StudentGroup> studentGroups = studentGroupRepository.findAll();
        model.addAttribute("studentGroups", studentGroups);
        return "newschedule";
    }


    @PostMapping("/addschedule")
    public String saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
        return "redirect:/schedule";
    }

    @GetMapping("/editschedule/{id}")
    public String editSchedule(@PathVariable("id") Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + id));
        model.addAttribute("schedule", schedule);
        return "editschedule";
    }

    @PostMapping("/editschedule/{id}")
    public String updateSchedule(@PathVariable("id") Long id, Schedule schedule, Model model) {
        scheduleRepository.save(schedule);
        return "redirect:/schedule";
    }

    @GetMapping("/deleteschedule/{id}")
    public String deleteSchedule(@PathVariable("id") Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + id));
        scheduleRepository.delete(schedule);
        return "redirect:/schedule";
    }

    @GetMapping("/teacher-schedule")
    public String showTeacherSchedule(@RequestParam int userId, Model model) {
        Optional<User> teacherOpt = userRepository.findById((long) userId);
        if (!teacherOpt.isPresent()) {
            model.addAttribute("error", "Пользователь с id " + userId + " не найден");
            return "authorisation";
        }
        User teacher = teacherOpt.get();
        model.addAttribute("userId", userId);
        List<Schedule> teacherSchedule = scheduleRepository.findByTeacherName(teacher.getName() + " " + teacher.getSurname());
        model.addAttribute("teacherSchedule", teacherSchedule);
        return "teacher-schedule";
    }

}
