package com.projects.library.services;

import com.projects.library.dto.request.AddUserRequest;
import com.projects.library.exception.UserNotFoundException;
import com.projects.library.exception.UserDeletionException;
import com.projects.library.model.Book;
import com.projects.library.model.User;
import com.projects.library.model.Loan;
import com.projects.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUser(long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<Book> getBooksLoanedByUser(long userId) {
        User user = getUser(userId);
        return user.getLoans().stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    public User addUser(AddUserRequest addUserRequest) {
        User user = new User(addUserRequest.firstName(), addUserRequest.lastName(), LocalDateTime.now());
        return repository.save(user);
    }

    public void deleteUser(long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        try {
            repository.delete(user);
        } catch (Exception ex) {
            throw new UserDeletionException(id);
        }
    }
}
