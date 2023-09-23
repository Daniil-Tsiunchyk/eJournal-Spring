package com.example.ejournal.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column
    private String role;

    @Column
    private LocalDateTime creationDate;

    @Column
    private String status;

    @Column
    private String groupNumber;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String subject;
}