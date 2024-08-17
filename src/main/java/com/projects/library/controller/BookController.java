package com.projects.library.controller;

import com.projects.library.dto.request.AddBookRequest;
import com.projects.library.dto.request.ChangeStatusRequest;
import com.projects.library.model.Book;
import com.projects.library.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBook(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody AddBookRequest request) {
        Book createdBook = bookService.addBook(request);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/status")
    public ResponseEntity<Void> changeBookStatus(@RequestBody ChangeStatusRequest request) {
        bookService.changeStatus(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/available/count")
    public ResponseEntity<Integer> getAvailableBooksCount() {
        int count = bookService.getAvailableBooksCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}