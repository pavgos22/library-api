package com.projects.library.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(long userId, String firstName, String lastName, LocalDateTime creationDate) {
}
