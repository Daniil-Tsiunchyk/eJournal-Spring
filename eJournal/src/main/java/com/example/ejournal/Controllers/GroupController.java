package com.example.ejournal.Controllers;

import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Repositories.StudentGroupRepository;
import com.example.ejournal.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GroupController {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/deleteGroup/{id}")
    public String deleteGroup(@PathVariable Long id) {
        studentGroupRepository.deleteById(id);
        return "redirect:/tablegroups";
    }

    @PostMapping("/editGroup/{id}")
    public String editGroup(@PathVariable Long id, @RequestParam("groupNumber") String groupNumber) {
        StudentGroup group = studentGroupRepository.findById(id).orElse(null);
        if (group != null) {
            group.setGroupNumber(groupNumber);
            studentGroupRepository.save(group);
        }
        return "redirect:/tablegroups";
    }

    @GetMapping("/tablegroups")
    public String showTableGroups(Model model) {
        List<StudentGroup> groups = studentGroupRepository.findAll();
        Map<String, Integer> membersCount = new HashMap<>();
        for (StudentGroup group : groups) {
            int count = userRepository.countUsersByGroupNumber(group.getGroupNumber());
            membersCount.put(group.getGroupNumber(), count);
        }
        model.addAttribute("groups", groups);
        model.addAttribute("membersCount", membersCount);
        return "tablegroups";
    }

}

