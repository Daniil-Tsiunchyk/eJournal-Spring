package com.example.ejournal.Models;

public class GroupStatistics {
    private String groupNumber;
    private String subject;
    private double averageMark;
    private int absenteeisms;

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(double averageMark) {
        this.averageMark = averageMark;
    }

    public int getAbsenteeisms() {
        return absenteeisms;
    }

    public void setAbsenteeisms(int absenteeisms) {
        this.absenteeisms = absenteeisms;
    }
}
