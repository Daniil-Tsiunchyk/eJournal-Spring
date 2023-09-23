package com.example.ejournal.Models;

import lombok.Data;

@Data
public class GroupStatistics {
    private String groupNumber;
    private String subject;
    private double averageMark;
    private int absenteeisms;
    private int totalStudents;
}
