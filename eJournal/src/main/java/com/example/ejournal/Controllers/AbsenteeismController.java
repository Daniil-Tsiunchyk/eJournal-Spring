package com.example.ejournal.Controllers;

import com.example.ejournal.Models.*;
import com.example.ejournal.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AbsenteeismController {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private AbsenteeismRepository absenteeismRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/setmark")
    public String setMarkPage(Model model) {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<User> students = userRepository.findByRole("student");
        model.addAttribute("schedules", schedules);
        model.addAttribute("students", students);
        return "setmark";
    }

    @PostMapping("/setmark")
    public String setMark(@RequestParam Long schedule, @RequestParam Long student, @RequestParam double mark) {
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
        }

        return "redirect:/setmark";
    }

    @GetMapping("/tableabsenteeism")
    public String showStudentTable(Model model) {
        List<StudentGroup> groups = studentGroupRepository.findAll();
        model.addAttribute("groups", groups);
        return "tableabsenteeism";
    }

    @GetMapping("/student-marks")
    public String getStudentMarks(@RequestParam("userId") int userId, Model model) {
        List<Mark> marks = markRepository.findByUserId(userId);
        model.addAttribute("marks", marks);
        model.addAttribute("userId", userId);
        return "student-marks";
    }

    @GetMapping("/student-absenteeism")
    public String getStudentAbsenteeism(@RequestParam("userId") int userId, Model model) {
        List<Absenteeism> absenteeisms = absenteeismRepository.findByUserId(userId);
        model.addAttribute("absenteeisms", absenteeisms);
        model.addAttribute("userId", userId);
        return "student-absenteeism";
    }
}


