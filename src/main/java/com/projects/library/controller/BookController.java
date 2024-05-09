package com.projects.library.controller;

import com.projects.library.model.Book;
import com.projects.library.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/library/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateBookStatus(@PathVariable int id, @RequestBody String status) {
        Book book = bookService.findBookById(id);
        if (book != null) {
            bookService.changeStatus(book, status);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
//        bookService.deleteBook(new Book(id));
//        return ResponseEntity.ok().build();
//    }
}
