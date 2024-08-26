package com.projects.library.model;

import com.projects.library.enums.BookStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.within;

@DataJpaTest
@ActiveProfiles("test")
public class UserTestSuite {

    @PersistenceContext
    private EntityManager entityManager;

    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        user = new User("Joe", "Nemo", LocalDateTime.now());
        entityManager.persist(user);

        Title title = new Title("Test Title", "Test Author", 2023);
        entityManager.persist(title);

        book = new Book(title, BookStatus.AVAILABLE);
        entityManager.persist(book);

        loan = new Loan(user, book, LocalDateTime.now());
        user.getLoans().add(loan);
        entityManager.persist(loan);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    void userCreationTest() {
        User foundUser = entityManager.find(User.class, user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isGreaterThan(0);
        assertThat(foundUser.getFirstName()).isEqualTo("Joe");
        assertThat(foundUser.getLastName()).isEqualTo("Nemo");
        assertThat(foundUser.getCreationDate()).isNotNull();

        assertThat(foundUser.getLoans()).hasSize(1);
        Loan foundLoan = foundUser.getLoans().iterator().next();
        assertThat(foundLoan.getBook().getId()).isEqualTo(book.getId());

        assertThat(foundLoan.getBorrowDate())
                .isCloseTo(loan.getBorrowDate(), within(1, ChronoUnit.SECONDS));

        assertThat(foundLoan.getUser().getId()).isEqualTo(user.getId());
    }



    @Test
    @Transactional
    void userUpdateTest() {
        String newFirstName = "John";
        user.setFirstName(newFirstName);
        user = entityManager.merge(user);
        entityManager.flush();

        User foundUser = entityManager.find(User.class, user.getId());
        assertThat(foundUser.getFirstName()).isEqualTo(newFirstName);
    }

    @Test
    @Transactional
    void userDeletionTest() {
        User userToDelete = entityManager.find(User.class, user.getId());
        entityManager.remove(userToDelete);
        entityManager.flush();
        entityManager.clear();

        User foundUser = entityManager.find(User.class, user.getId());
        assertThat(foundUser).isNull();

        Loan foundLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(foundLoan).isNull();
    }

    @Test
    @Transactional
    void loanDeletionWhenUserIsDeletedTest() {
        User userToDelete = entityManager.find(User.class, user.getId());
        entityManager.remove(userToDelete);
        entityManager.flush();
        entityManager.clear();

        Loan foundLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(foundLoan).isNull();
    }

    @Test
    @Transactional
    void addingLoansToUserTest() {
        Book newBook = new Book(book.getTitle(), BookStatus.AVAILABLE);
        entityManager.persist(newBook);

        Loan newLoan = new Loan(user, newBook, LocalDateTime.now());
        user.getLoans().add(newLoan);
        entityManager.persist(newLoan);
        entityManager.flush();

        User foundUser = entityManager.find(User.class, user.getId());
        assertThat(foundUser.getLoans()).contains(newLoan);
    }
}
