package com.example.ejournal.Repositories;

import com.example.ejournal.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);

    @Query("SELECT COUNT(u) FROM User u WHERE u.groupNumber = :group_number")
    Integer countUsersByGroupNumber(@Param("group_number") String group_number);

    List<User> findAllByRole(String role);

    List<User> findAllByRoleAndGroupNumber(String role, String groupNumber);

    List<User> findByRole(String role);

    List<User> findAllByGroupNumber(String groupNumber);


}
