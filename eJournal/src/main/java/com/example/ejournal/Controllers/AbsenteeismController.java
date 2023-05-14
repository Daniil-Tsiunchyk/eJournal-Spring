package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Absenteeism;
import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.AbsenteeismRepository;
import com.example.ejournal.Repositories.StudentGroupRepository;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AbsenteeismController {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private AbsenteeismRepository absenteeismRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/tableabsenteeism")
    public String showStudentTable(@RequestParam(required = false) Integer userId, Model model) {
        List<User> users = (List<User>) userRepository.findAll();
        model.addAttribute("users", users);

        List<StudentGroup> groups = studentGroupRepository.findAll();
        model.addAttribute("groups", groups);

        if (userId != null) {
            List<Absenteeism> absences = absenteeismRepository.findByUserId(userId);
            model.addAttribute("absences", absences);
        }
        return "tableabsenteeism";
    }


    @GetMapping("/student-absenteeism")
    public String getStudentAbsenteeism(@RequestParam("userId") int userId, Model model) {
        List<Absenteeism> absenteeisms = absenteeismRepository.findByUserId(userId);
        model.addAttribute("absenteeisms", absenteeisms);
        model.addAttribute("userId", userId);
        return "student-absenteeism";
    }

    @RequestMapping("/absenteeism/{userId}")
    public String showAbsencesByUser(@PathVariable Long userId, Model model) {
        model.addAttribute("absences", absenteeismRepository.findByUserId(Math.toIntExact(userId)));
        return "tableabsenteeism";
    }
}


