package com.projects.library.service;

import com.projects.library.dto.request.AddLoanRequest;
import com.projects.library.dto.response.LoanResponse;
import com.projects.library.enums.BookStatus;
import com.projects.library.exception.BookNotFoundException;
import com.projects.library.exception.LoanNotFoundException;
import com.projects.library.exception.UserNotFoundException;
import com.projects.library.model.Book;
import com.projects.library.model.Loan;
import com.projects.library.model.Title;
import com.projects.library.model.User;
import com.projects.library.repository.BookRepository;
import com.projects.library.repository.LoanRepository;
import com.projects.library.repository.TitleRepository;
import com.projects.library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LoanServiceTestSuite {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TitleRepository titleRepository;

    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        titleRepository.deleteAll();

        user = new User("Joe", "Nemo", LocalDateTime.now());
        userRepository.save(user);

        Title title = new Title("Test Title", "Test Author", 2024);
        titleRepository.save(title);

        book = new Book(title, BookStatus.AVAILABLE);
        bookRepository.save(book);
    }

    @Test
    void shouldGetAllLoans() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        List<LoanResponse> loans = loanService.getAllLoans();

        assertThat(loans).hasSize(1);
        assertThat(loans.get(0).id()).isEqualTo(loan.getId());
    }

    @Test
    void shouldGetLoanById() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        LoanResponse foundLoan = loanService.getLoan(loan.getId());

        assertThat(foundLoan).isNotNull();
        assertThat(foundLoan.id()).isEqualTo(loan.getId());
    }

    @Test
    void shouldThrowLoanNotFoundExceptionWhenLoanNotFound() {
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoan(999L));
    }

    @Test
    void shouldRentBook() {
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        AddLoanRequest request = new AddLoanRequest(user.getId(), book.getId());

        loanRepository.deleteAll();
        LoanResponse newLoan = loanService.rentBook(request);

        assertThat(newLoan).isNotNull();
        assertThat(newLoan.book().id()).isEqualTo(book.getId());
        assertThat(newLoan.user().userId()).isEqualTo(user.getId());

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updatedBook.getStatus()).isEqualTo(BookStatus.RENTED);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        AddLoanRequest request = new AddLoanRequest(999L, book.getId());

        assertThrows(UserNotFoundException.class, () -> loanService.rentBook(request));
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenBookNotFound() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        AddLoanRequest request = new AddLoanRequest(user.getId(), 999L);

        assertThrows(BookNotFoundException.class, () -> loanService.rentBook(request));
    }

    @Test
    void shouldReturnBook() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);
        book.setLoan(loan);
        bookRepository.save(book);

        loanService.bookReturn(book.getId());

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updatedBook.getStatus()).isEqualTo(BookStatus.AVAILABLE);

        Loan updatedLoan = loanRepository.findById(loan.getId()).orElseThrow();
        assertThat(updatedLoan.getReturnDate()).isNotNull();
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenReturningNonExistentBook() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        assertThrows(BookNotFoundException.class, () -> loanService.bookReturn(999L));
    }

    @Test
    void shouldDeleteLoan() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        loanService.deleteLoan(loan.getId());

        assertThrows(LoanNotFoundException.class, () -> loanService.getLoan(loan.getId()));
        assertThat(loanRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowLoanNotFoundExceptionWhenDeletingNonExistentLoan() {
        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        assertThrows(LoanNotFoundException.class, () -> loanService.deleteLoan(999L));
    }
}
