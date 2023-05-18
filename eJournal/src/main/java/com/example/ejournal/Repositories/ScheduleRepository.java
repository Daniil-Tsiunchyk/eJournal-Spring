package com.example.ejournal.Repositories;

import com.example.ejournal.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByGroupNumber(String groupNumber);

    List<Schedule> findByTeacherName(String teacherName);

    List<Schedule> findAllByGroupNumber(String groupNumber);

}
