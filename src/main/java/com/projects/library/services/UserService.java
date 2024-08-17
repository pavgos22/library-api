package com.projects.library.services;

import com.projects.library.dto.request.AddUserRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.dto.response.UserResponse;
import com.projects.library.exception.UserNotFoundException;
import com.projects.library.exception.UserDeletionException;
import com.projects.library.mapper.BookMapper;
import com.projects.library.mapper.UserMapper;
import com.projects.library.model.Book;
import com.projects.library.model.User;
import com.projects.library.model.Loan;
import com.projects.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public List<UserResponse> getAllUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUser(long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toUserResponse(user);
    }

    public List<BookResponse> getBooksLoanedByUser(long userId) {
        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return user.getLoans().stream()
                .map(Loan::getBook)
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }

    public UserResponse addUser(AddUserRequest addUserRequest) {
        User user = new User(addUserRequest.firstName(), addUserRequest.lastName(), LocalDateTime.now());
        User savedUser = repository.save(user);
        return userMapper.toUserResponse(savedUser);
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
