package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Absenteeism;
import com.example.ejournal.Models.Mark;
import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Repositories.AbsenteeismRepository;
import com.example.ejournal.Repositories.MarkRepository;
import com.example.ejournal.Repositories.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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


