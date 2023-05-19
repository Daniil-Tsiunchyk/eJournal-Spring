package com.example.ejournal.Controllers;

import com.example.ejournal.Models.*;
import com.example.ejournal.Repositories.AbsenteeismRepository;
import com.example.ejournal.Repositories.MarkRepository;
import com.example.ejournal.Repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @Autowired
    private ScheduleRepository scheduleRepository;

    @RequestMapping("/statistics")
    public String getStatistics(Model model) {
        Map<String, GroupStatistics> groupStatisticsMap = new HashMap<>();

        List<Schedule> schedules = scheduleRepository.findAll();
        for (Schedule schedule : schedules) {
            String key = schedule.getGroupNumber() + "-" + schedule.getSubject();
            GroupStatistics groupStatistics = groupStatisticsMap.getOrDefault(key, new GroupStatistics());
            groupStatistics.setGroupNumber(schedule.getGroupNumber());
            groupStatistics.setSubject(schedule.getSubject());

            List<Mark> marks = markRepository.findBySubjectAndUserId(schedule.getSubject(), Math.toIntExact(schedule.getId()));
            double averageMark = marks.stream().mapToDouble(Mark::getMark).average().orElse(0);
            if (groupStatisticsMap.containsKey(key)) {
                averageMark = (groupStatistics.getAverageMark() * groupStatistics.getTotalStudents() + averageMark) / (groupStatistics.getTotalStudents() + 1);
            }
            groupStatistics.setAverageMark(averageMark);

            List<Absenteeism> absenteeisms = absenteeismRepository.findBySubjectAndUserId(schedule.getSubject(), Math.toIntExact(schedule.getId()));
            groupStatistics.setAbsenteeisms(groupStatistics.getAbsenteeisms() + absenteeisms.size());

            groupStatistics.setTotalStudents(groupStatistics.getTotalStudents() + 1);

            groupStatisticsMap.put(key, groupStatistics);
        }

        model.addAttribute("groupStatistics", new ArrayList<>(groupStatisticsMap.values()));
        return "statistics";
    }




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
        return "tablestatistics";
    }
}