package com.example.ejournal.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "absenteeisms")
public class Absenteeism {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private int userId;
}
