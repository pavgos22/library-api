package com.projects.library.dto.request;

import com.projects.library.enums.BookStatus;

public record ChangeStatusRequest(BookStatus status) {
}
