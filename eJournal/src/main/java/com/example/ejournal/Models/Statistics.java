package com.example.ejournal.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subject;
    private double averageMark;
    private int absenteeisms;
}