package com.projects.library.dto.response;

import java.util.Set;

public record UserResponse(long userId, String firstName, String lastName, String creationDate, Set<LoanResponse> loans) {
}
