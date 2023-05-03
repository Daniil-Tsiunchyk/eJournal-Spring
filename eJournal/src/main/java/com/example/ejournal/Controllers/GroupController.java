package com.example.ejournal.Controllers;

import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Repositories.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GroupController {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @GetMapping("/newgroup")
    public String showNewGroupForm() {
        return "newgroup";
    }

    @PostMapping("/saveStudentGroup")
    public String saveStudentGroup(@RequestParam(value = "groupNumber", required = false) String groupNumber, Model model, RedirectAttributes redirectAttributes) {
        if (groupNumber == null || groupNumber.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ошибка!");
            return "redirect:/newgroup";
        }
        List<StudentGroup> existingGroups = studentGroupRepository.findByGroupNumber(groupNumber);
        if (!existingGroups.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Такая группа уже существует!");
            return "redirect:/newgroup";
        }
        StudentGroup newGroup = new StudentGroup();
        newGroup.setGroupNumber(groupNumber);
        studentGroupRepository.save(newGroup);
        model.addAttribute("message", "Группа сохранена с ID: " + newGroup.getId());
        return "newgroup";
    }

}
