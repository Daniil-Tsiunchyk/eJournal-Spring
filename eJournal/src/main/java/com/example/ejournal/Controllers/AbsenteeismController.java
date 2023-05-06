package com.example.ejournal.Controllers;

import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Repositories.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AbsenteeismController {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @GetMapping("/tableabsenteeism")
    public String showStudentTable(Model model) {
        List<StudentGroup> groups = studentGroupRepository.findAll();
        model.addAttribute("groups", groups);
        return "tableabsenteeism";
    }
}


