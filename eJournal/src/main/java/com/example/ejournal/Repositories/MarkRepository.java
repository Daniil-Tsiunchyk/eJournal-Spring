package com.example.ejournal.Repositories;

import com.example.ejournal.Models.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByUserId(int userId);
}
