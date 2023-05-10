package com.example.ejournal.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subject;
    private double averageMark;
    private int absenteeisms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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