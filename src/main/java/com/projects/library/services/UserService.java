package com.projects.library.services;

import com.projects.library.model.User;
import com.projects.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(final User user) {
        return repository.save(user);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }
}