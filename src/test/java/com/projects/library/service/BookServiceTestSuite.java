package com.projects.library.service;

import com.projects.library.dto.request.ChangeStatusRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.enums.BookStatus;
import com.projects.library.exception.BookNotFoundException;
import com.projects.library.exception.NullBookStatusException;
import com.projects.library.exception.TitleNotFoundException;
import com.projects.library.model.Book;
import com.projects.library.model.Title;
import com.projects.library.repository.BookRepository;
import com.projects.library.repository.TitleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookServiceTestSuite {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TitleRepository titleRepository;

    private Title title;
    private Book book;

    @BeforeEach
    void setUp() {
        title = new Title("Test Title", "Test Author", 2024);
        titleRepository.save(title);

        book = new Book(title, BookStatus.AVAILABLE);
        bookRepository.save(book);
    }

    @Test
    void shouldGetAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();

        assertThat(books).hasSize(1);
        assertThat(books.get(0).id()).isEqualTo(book.getId());
    }

    @Test
    void shouldGetAvailableBooks() {
        List<BookResponse> availableBooks = bookService.getAvailableBooks(null);

        assertThat(availableBooks).hasSize(1);
        assertThat(availableBooks.get(0).id()).isEqualTo(book.getId());
    }

    @Test
    void shouldGetAvailableBooksByTitleId() {
        List<BookResponse> availableBooks = bookService.getAvailableBooks(title.getId());

        assertThat(availableBooks).hasSize(1);
        assertThat(availableBooks.get(0).id()).isEqualTo(book.getId());
    }

    @Test
    void shouldGetBook() {
        BookResponse foundBook = bookService.getBook(book.getId());

        assertThat(foundBook).isNotNull();
        assertThat(foundBook.id()).isEqualTo(book.getId());
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenBookNotFound() {
        assertThrows(BookNotFoundException.class, () -> bookService.getBook(999L));
    }

    @Test
    void shouldAddBook() {
        BookResponse newBook = bookService.addBook(title.getId());

        assertThat(newBook).isNotNull();
        assertThat(bookRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldThrowTitleNotFoundExceptionWhenAddingBookWithInvalidTitleId() {
        assertThrows(TitleNotFoundException.class, () -> bookService.addBook(999L));
    }

    @Test
    void shouldDeleteBook() {
        bookService.deleteBook(book.getId());

        assertThrows(BookNotFoundException.class, () -> bookService.getBook(book.getId()));
        assertThat(bookRepository.findAll()).isEmpty();
    }

    @Test
    void shouldChangeBookStatus() {
        ChangeStatusRequest request = new ChangeStatusRequest(BookStatus.RENTED);

        bookService.changeStatus(book.getId(), request);

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updatedBook.getStatus()).isEqualTo(BookStatus.RENTED);
    }

    @Test
    void shouldThrowNullBookStatusExceptionWhenStatusIsNull() {
        ChangeStatusRequest request = new ChangeStatusRequest(null);

        assertThrows(NullBookStatusException.class, () -> bookService.changeStatus(book.getId(), request));
    }

    @Test
    void shouldGetAvailableBooksCount() {
        int count = bookService.getAvailableBooksCount();

        assertThat(count).isEqualTo(1);
    }
}
