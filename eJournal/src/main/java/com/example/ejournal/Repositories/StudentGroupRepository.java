package com.example.ejournal.Repositories;

import com.example.ejournal.Models.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
    List<StudentGroup> findByGroupNumber(String groupNumber);
    List<StudentGroup> findAll();
}
