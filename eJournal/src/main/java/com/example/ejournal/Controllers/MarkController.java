package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Mark;
import com.example.ejournal.Models.Schedule;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.MarkRepository;
import com.example.ejournal.Repositories.ScheduleRepository;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MarkController {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/setmark")
    public String setMarkPage(@RequestParam("userId") Long userId, Model model) {
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

        return "setmark";
    }


    @PostMapping("/setmark")
    public String setMark(@RequestParam Long schedule, @RequestParam Long student, @RequestParam double mark, @RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
        Schedule selectedSchedule = scheduleRepository.findById(schedule).orElse(null);
        User selectedStudent = userRepository.findById(student).orElse(null);

        if (selectedSchedule != null && selectedStudent != null) {
            Mark newMark = new Mark();
            newMark.setSubject(selectedSchedule.getSubject());
            newMark.setTime(selectedSchedule.getTime());
            newMark.setDate(selectedSchedule.getDayOfWeek());
            newMark.setUserId(selectedStudent.getId().intValue());
            newMark.setMark(mark);

            markRepository.save(newMark);

            redirectAttributes.addFlashAttribute("success", "Оценка " + mark + " была успешно добавлена студенту " + selectedStudent.getName() + " " + selectedStudent.getSurname());
        }

        return "redirect:/setmark?userId=" + userId;
    }


    @GetMapping("/student-marks")
    public String getStudentMarks(@RequestParam("userId") int userId, Model model) {
        List<Mark> marks = markRepository.findByUserId(userId);
        model.addAttribute("marks", marks);
        model.addAttribute("userId", userId);
        return "student-marks";
    }

    @GetMapping("/tablestudents/{groupNumber}")
    public String showStudentsByGroup(@PathVariable String groupNumber, Model model) {
        List<User> users = userRepository.findByGroupNumber(groupNumber);

        model.addAttribute("students", users);

        return "tablestudents";
    }

}


