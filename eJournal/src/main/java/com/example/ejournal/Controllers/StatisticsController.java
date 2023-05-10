package com.example.ejournal.Controllers;

import com.example.ejournal.Models.Absenteeism;
import com.example.ejournal.Models.Mark;
import com.example.ejournal.Models.Statistics;
import com.example.ejournal.Repositories.AbsenteeismRepository;
import com.example.ejournal.Repositories.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatisticsController {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private AbsenteeismRepository absenteeismRepository;

    @GetMapping("/student-statistics/{userId}")
    public String getStatistics(@PathVariable("userId") int userId, Model model) {
        List<Mark> marks = markRepository.findByUserId(userId);
        List<Absenteeism> absenteeisms = absenteeismRepository.findByUserId(userId);

        Map<String, List<Double>> subjectMarks = new HashMap<>();
        Map<String, Integer> subjectAbsenteeisms = new HashMap<>();

        for (Mark mark : marks) {
            String subject = mark.getSubject();
            if (!subjectMarks.containsKey(subject)) {
                subjectMarks.put(subject, new ArrayList<>());
            }
            subjectMarks.get(subject).add(mark.getMark());
        }

        for (Absenteeism absenteeism : absenteeisms) {
            String subject = absenteeism.getSubject();
            subjectAbsenteeisms.put(subject, subjectAbsenteeisms.getOrDefault(subject, 0) + 1);
        }

        List<Statistics> statisticsList = new ArrayList<>();
        for (String subject : subjectMarks.keySet()) {
            Statistics statistics = new Statistics();
            statistics.setSubject(subject);

            List<Double> marksList = subjectMarks.get(subject);
            double sum = 0;
            for (double mark : marksList) {
                sum += mark;
            }
            double averageMark = sum / marksList.size();
            statistics.setAverageMark(averageMark);

            int absenteeismCount = subjectAbsenteeisms.getOrDefault(subject, 0);
            statistics.setAbsenteeisms(absenteeismCount);

            statisticsList.add(statistics);
        }

        model.addAttribute("userId", userId);
        model.addAttribute("statistics", statisticsList);
        return "statistics";
    }
}