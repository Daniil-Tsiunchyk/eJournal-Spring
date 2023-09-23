package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Absenteeism;
import com.example.ejournal.Models.Schedule;
import com.example.ejournal.Models.StudentGroup;
import com.example.ejournal.Models.User;
import com.example.ejournal.Repositories.AbsenteeismRepository;
import com.example.ejournal.Repositories.ScheduleRepository;
import com.example.ejournal.Repositories.StudentGroupRepository;
import com.example.ejournal.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AbsenteeismController {

    private final StudentGroupRepository studentGroupRepository;

    private final AbsenteeismRepository absenteeismRepository;

    private final UserRepository userRepository;

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public AbsenteeismController(StudentGroupRepository studentGroupRepository, AbsenteeismRepository absenteeismRepository, UserRepository userRepository, ScheduleRepository scheduleRepository) {
        this.studentGroupRepository = studentGroupRepository;
        this.absenteeismRepository = absenteeismRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

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

    @GetMapping("/setabsenteeism")
    public String setAbsenteeismPage(Model model, HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null) {
            return "authorisation";
        }
        model.addAttribute("userId", teacher.getId());
        model.addAttribute("teacher", teacher);

        List<Schedule> schedules = scheduleRepository.findBySubject(teacher.getSubject().trim());
        model.addAttribute("schedules", schedules);

        List<User> students = userRepository.findAllByRole("student");
        model.addAttribute("students", students);

        return "setabsenteeism";
    }


    @PostMapping("/setabsenteeism")
    public String setAbsenteeism(@RequestParam Long schedule, @RequestParam Long student, RedirectAttributes redirectAttributes, HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null) {
            return "authorisation";
        }

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

        return "redirect:/setabsenteeism";
    }

}


