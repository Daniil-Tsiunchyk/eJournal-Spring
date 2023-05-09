package com.example.ejournal.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrefectController {
    @GetMapping("/teacher-schedule")
    public String showPrefectSchedule(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        return "teacher-schedule";
    }

    @GetMapping("/setmark")
    public String showSetMark(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        return "setmark";
    }

    @GetMapping("/setabsenteeism")
    public String showSetAbsenteeism(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        return "setabsenteeism";
    }
}
