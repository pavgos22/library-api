package com.projects.library.dto.response;

import com.projects.library.enums.BookStatus;

public record BookResponse(long id, String title, BookStatus status, LoanResponse loan) {
}
