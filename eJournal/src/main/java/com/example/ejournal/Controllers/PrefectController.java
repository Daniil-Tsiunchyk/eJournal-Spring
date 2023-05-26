package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Absenteeism;
import com.example.ejournal.Models.Schedule;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.AbsenteeismRepository;
import com.example.ejournal.Repositories.ScheduleRepository;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PrefectController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AbsenteeismRepository absenteeismRepository;

    @GetMapping("/teacher-schedule")
    public String showTeacherSchedule(@RequestParam int userId, Model model) {
        Optional<User> teacherOpt = userRepository.findById((long) userId);
        if (!teacherOpt.isPresent()) {
            model.addAttribute("error", "Пользователь с id " + userId + " не найден");
            return "authorisation";
        }
        User teacher = teacherOpt.get();
        model.addAttribute("userId", userId);
        model.addAttribute("teacher", teacher);

        List<Schedule> teacherSchedule = scheduleRepository.findBySubject(teacher.getSubject().trim());
        model.addAttribute("teacherSchedule", teacherSchedule);

        List<String> days = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
        List<String> times = Arrays.asList("09:00", "10:35", "12:25", "14:00", "15:50", "17:25");
        model.addAttribute("days", days);
        model.addAttribute("times", times);

        return "teacher-schedule";
    }


    @GetMapping("/setabsenteeism")
    public String setAbsenteeismPage(@RequestParam("userId") Long userId, Model model) {
        Optional<User> teacherOpt = userRepository.findById(userId);
        if (teacherOpt.isEmpty()) {
            model.addAttribute("error", "Пользователь с id " + userId + " не найден");
            return "authorisation";
        }
        User teacher = teacherOpt.get();
        model.addAttribute("userId", userId);
        model.addAttribute("teacher", teacher);

        List<Schedule> schedules = scheduleRepository.findBySubject(teacher.getSubject().trim());
        model.addAttribute("schedules", schedules);

        List<User> students = userRepository.findByRole("student");
        model.addAttribute("students", students);

        return "setabsenteeism";
    }


    @PostMapping("/setabsenteeism")
    public String setAbsenteeism(@RequestParam Long schedule, @RequestParam Long student, @RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
        Schedule selectedSchedule = scheduleRepository.findById(schedule).orElse(null);
        User selectedStudent = userRepository.findById(student).orElse(null);

        if (selectedSchedule != null && selectedStudent != null) {
            Absenteeism newAbsenteeism = new Absenteeism();
            newAbsenteeism.setSubject(selectedSchedule.getSubject());
            newAbsenteeism.setTime(selectedSchedule.getTime());
            newAbsenteeism.setDate(selectedSchedule.getDayOfWeek());
            newAbsenteeism.setUserId(selectedStudent.getId().intValue());

            absenteeismRepository.save(newAbsenteeism);
            redirectAttributes.addFlashAttribute("success", "Прогул успешно записан для студента " + selectedStudent.getName() + " " + selectedStudent.getSurname());
        }

        return "redirect:/setabsenteeism?userId=" + userId;
    }


}
