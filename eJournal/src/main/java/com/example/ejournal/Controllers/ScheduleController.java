package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Schedule;
import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.ScheduleRepository;
import com.example.ejournal.Repositories.StudentGroupRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;

    private final StudentGroupRepository studentGroupRepository;

    @Autowired
    public ScheduleController(ScheduleRepository scheduleRepository, StudentGroupRepository studentGroupRepository) {
        this.scheduleRepository = scheduleRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    @GetMapping("/schedule")
    public String showSchedule(@RequestParam(required = false) String groupNumber, Model model) {
        List<Schedule> schedules;
        if (groupNumber == null) {
            schedules = scheduleRepository.findAll();
        } else {
            schedules = scheduleRepository.findByGroupNumber(groupNumber);
        }
        model.addAttribute("schedules", schedules);
        List<StudentGroup> groups = studentGroupRepository.findAll();
        model.addAttribute("groups", groups);
        List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
        List<String> times = Arrays.asList("09:00", "10:35", "12:25", "14:00", "15:50", "17:25");
        model.addAttribute("days", days);
        model.addAttribute("times", times);
        return "tableschedule";
    }


    @GetMapping("/schedule/newschedule")
    public String addSchedule(Model model) {
        List<StudentGroup> studentGroups = studentGroupRepository.findAll();
        model.addAttribute("studentGroups", studentGroups);
        return "newschedule";
    }


    @PostMapping("/schedule/addschedule")
    public String saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
        return "redirect:/schedule";
    }

    @GetMapping("/schedule/editschedule/{id}")
    public String editSchedule(@PathVariable("id") Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + id));
        model.addAttribute("schedule", schedule);
        return "editschedule";
    }

    @PostMapping("/schedule/editschedule/{id}")
    public String updateSchedule(@PathVariable("id") Long id, @ModelAttribute Schedule updatedSchedule) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный Id:" + id));
        schedule.setSubject(updatedSchedule.getSubject());
        schedule.setTime(updatedSchedule.getTime());
        schedule.setDayOfWeek(updatedSchedule.getDayOfWeek());
        schedule.setGroupNumber(updatedSchedule.getGroupNumber());
        schedule.setTeacherName(updatedSchedule.getTeacherName());
        scheduleRepository.save(schedule);
        return "redirect:/schedule";
    }


    @GetMapping("/schedule/deleteschedule/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + id));
        scheduleRepository.delete(schedule);
        return "redirect:/schedule";
    }

    @GetMapping("/teacher-schedule")
    public String showTeacherSchedule(Model model, HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null) {
            return "authorisation";
        }
        model.addAttribute("userId", teacher.getId());
        model.addAttribute("teacher", teacher);

        List<Schedule> teacherSchedule = scheduleRepository.findBySubject(teacher.getSubject().trim());
        model.addAttribute("teacherSchedule", teacherSchedule);

        List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
        List<String> times = Arrays.asList("09:00", "10:35", "12:25", "14:00", "15:50", "17:25");
        model.addAttribute("days", days);
        model.addAttribute("times", times);

        return "teacher-schedule";
    }
}