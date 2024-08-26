package com.projects.library.service;

import com.projects.library.dto.request.AddUserRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.dto.response.UserResponse;
import com.projects.library.exception.UserNotFoundException;
import com.projects.library.model.User;
import com.projects.library.model.Loan;
import com.projects.library.model.Book;
import com.projects.library.model.Title;
import com.projects.library.repository.UserRepository;
import com.projects.library.repository.LoanRepository;
import com.projects.library.repository.BookRepository;
import com.projects.library.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTestSuite {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

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

        book = new Book(title, com.projects.library.enums.BookStatus.AVAILABLE);
        bookRepository.save(book);

        loan = new Loan(user, book, LocalDateTime.now());
        loanRepository.save(loan);

        assertThat(userRepository.findById(user.getId())).isPresent();
        assertThat(bookRepository.findById(book.getId())).isPresent();
        assertThat(loanRepository.findById(loan.getId())).isPresent();
    }

    @Test
    void shouldGetAllUsers() {
        List<UserResponse> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).userId()).isEqualTo(user.getId());
    }

    @Test
    void shouldAddUser() {
        AddUserRequest request = new AddUserRequest("John", "Johnson");
        UserResponse newUser = userService.addUser(request);

        assertThat(newUser).isNotNull();
        assertThat(newUser.firstName()).isEqualTo(request.firstName());
        assertThat(newUser.lastName()).isEqualTo(request.lastName());
    }

    @Test
    void shouldGetUserById() {
        UserResponse foundUser = userService.getUser(user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.userId()).isEqualTo(user.getId());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(999L));
    }

    @Test
    void shouldGetBooksLoanedByUser() {
        user.getLoans().add(loan);

        List<BookResponse> loanedBooks = userService.getBooksLoanedByUser(user.getId());

        assertThat(loanedBooks).hasSize(1);
        assertThat(loanedBooks.get(0).id()).isEqualTo(book.getId());
    }

    @Test
    void shouldDeleteUser() {
        userService.deleteUser(user.getId());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(user.getId()));
        assertThat(userRepository.findAll()).isEmpty();
    }
}
