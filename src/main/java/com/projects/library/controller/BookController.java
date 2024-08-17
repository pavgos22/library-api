package com.projects.library.controller;

import com.projects.library.dto.request.ChangeStatusRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.service.BookService;
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
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.getBook(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks(@RequestParam(required = false) Long titleId) {
        List<BookResponse> availableBooks = bookService.getAvailableBooks(titleId);
        return new ResponseEntity<>(availableBooks, HttpStatus.OK);
    }

    @GetMapping("/available/count")
    public ResponseEntity<Integer> getAvailableBooksCount() {
        int count = bookService.getAvailableBooksCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping("/{titleId}")
    public ResponseEntity<BookResponse> addBook(@PathVariable long titleId) {
        BookResponse createdBook = bookService.addBook(titleId);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeBookStatus(@PathVariable long id, @RequestBody ChangeStatusRequest request) {
        bookService.changeStatus(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}