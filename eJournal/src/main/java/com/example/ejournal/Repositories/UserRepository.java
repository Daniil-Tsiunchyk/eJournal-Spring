package com.example.ejournal.Repositories;

import com.example.ejournal.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    @Query("SELECT COUNT(u) FROM User u WHERE u.groupNumber = :group_number")
    Integer countUsersByGroupNumber(@Param("group_number") String group_number);
    List<User> findByGroupNumberAndRole(String groupNumber, String role);
}