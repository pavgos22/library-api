package com.projects.library.dto.response;

import java.time.LocalDateTime;

public record LoanResponse(long id, UserResponse user, BookResponse book, LocalDateTime borrowDate, LocalDateTime returnDate) {
}
