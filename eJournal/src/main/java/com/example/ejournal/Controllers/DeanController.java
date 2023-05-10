package com.example.ejournal.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeanController {

    @GetMapping("/tableschedule")
    public String showTableSchedule(Model model) {
        return "tableschedule";
    }
}
