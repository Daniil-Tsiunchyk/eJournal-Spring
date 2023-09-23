package com.example.ejournal.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "student_group")
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "group_number")
    private String groupNumber;
}
