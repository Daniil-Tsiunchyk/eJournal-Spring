package com.example.ejournal.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private String groupNumber;

    @Column(nullable = false)
    private String teacherName;
}
