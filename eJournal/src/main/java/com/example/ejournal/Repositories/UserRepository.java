package com.example.ejournal.Repositories;

import com.example.ejournal.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {


    @Query("SELECT COUNT(u) FROM User u WHERE u.groupNumber = :group_number")
    Integer countUsersByGroupNumber(@Param("group_number") String group_number);

    List<User> findAllByRole(String role);

    List<User> findAllByRoleAndGroupNumber(String role, String groupNumber);

    List<User> findAllByGroupNumber(String groupNumber);

    User findByEmailAndPassword(String email, String password);

    User findByLoginAndPassword(String login, String password);

}
