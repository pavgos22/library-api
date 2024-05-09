package com.projects.library.services;

import com.projects.library.model.Book;
import com.projects.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private BookRepository repository;

    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    public Book saveBook(final Book book) {
        return repository.save(book);
    }

    public void deleteBook(Book book) {
        repository.delete(book);
    }

    public void changeStatus(Book book, String status) {
        repository.findById(book.getId())
                .ifPresent(b -> {
                    b.setStatus(status);
                    repository.save(b);
                });
    }

    public int getAvailableBooksCount() {
        return repository.countByStatus("Available");
    }

    public Book findBookById(int id) {
        return repository.findById(id).orElse(null);
    }
}
