package com.projects.library.repository;

import com.projects.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByFirstNameAndLastName(String firstName, String lastName);
}
