package com.projects.library.mapper;

import com.projects.library.dto.response.BookResponse;
import com.projects.library.model.Book;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final LoanMapper loanMapper;

    public BookMapper(@Lazy LoanMapper loanMapper) {
        this.loanMapper = loanMapper;
    }

    public BookResponse toBookResponse(Book book) {
        if (book == null) {
            return null;
        }
        return new BookResponse(
                book.getId(),
                book.getTitle().getTitle(),
                book.getStatus()
        );
    }

    public List<BookResponse> mapToBookResponseList(List<Book> books) {
        return books.stream()
                .map(this::toBookResponse)
                .collect(Collectors.toList());
    }
}
