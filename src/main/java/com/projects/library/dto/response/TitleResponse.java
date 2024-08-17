package com.projects.library.dto.response;

import java.util.Set;

public record TitleResponse(long id, String title, String author, int year, Set<BookResponse> books) {
}
