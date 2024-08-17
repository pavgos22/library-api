package com.projects.library.service;

import com.projects.library.dto.request.AddBookRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.dto.request.ChangeStatusRequest;
import com.projects.library.enums.BookStatus;
import com.projects.library.exception.BookNotFoundException;
import com.projects.library.exception.NullBookStatusException;
import com.projects.library.exception.TitleNotFoundException;
import com.projects.library.mapper.BookMapper;
import com.projects.library.model.Book;
import com.projects.library.model.Title;
import com.projects.library.repository.BookRepository;
import com.projects.library.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final TitleRepository titleRepository;
    private final BookMapper bookMapper;

    public List<BookResponse> getAllBooks() {
        List<Book> books = repository.findAll();
        return bookMapper.mapToBookResponseList(books);
    }

    public List<BookResponse> getAvailableBooks(Long titleId) {
        List<Book> availableBooks;

        if (titleId != null)
            availableBooks = repository.findByStatusAndTitleId(BookStatus.AVAILABLE, titleId);
        else availableBooks = repository.findByStatus(BookStatus.AVAILABLE);

        return availableBooks.stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }


    public BookResponse getBook(long id) {
        Book book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toBookResponse(book);
    }

    public BookResponse addBook(AddBookRequest request) {
        Title title = titleRepository.findById(request.titleId()).orElseThrow(() -> new TitleNotFoundException(request.titleId()));

        Book book = new Book(title, BookStatus.AVAILABLE);
        Book savedBook = repository.save(book);
        return bookMapper.toBookResponse(savedBook);
    }

    public void deleteBook(long id) {
        Book book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        repository.delete(book);
    }

    public void changeStatus(ChangeStatusRequest request) {
        if (request.status() == null)
            throw new NullBookStatusException();

        Book book = repository.findById(request.bookId()).orElseThrow(() -> new BookNotFoundException(request.bookId()));
        book.setStatus(request.status());
        repository.save(book);
    }

    public int getAvailableBooksCount() {
        return repository.countByStatus(BookStatus.AVAILABLE);
    }
}
