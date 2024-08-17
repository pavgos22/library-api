package com.projects.library.mapper;

import com.projects.library.dto.response.UserResponse;
import com.projects.library.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final LoanMapper loanMapper;

    public UserMapper(@Lazy LoanMapper loanMapper) {
        this.loanMapper = loanMapper;
    }

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreationDate().toString(),
                user.getLoans().stream().map(loanMapper::toLoanResponse).collect(Collectors.toSet())
        );
    }

    public Set<UserResponse> mapToUserResponseSet(Set<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toSet());
    }
}
