package com.example.ejournal.Repositories;

import com.example.ejournal.Models.Absenteeism;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AbsenteeismRepository extends JpaRepository<Absenteeism, Long> {
    List<Absenteeism> findByUserId(int userId);
    List<Absenteeism> findBySubjectAndUserId(String subject, int userId);

}
