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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LoanTestSuite {

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
        book.setLoan(loan);
        entityManager.persist(loan);

        entityManager.flush();
    }

    @Test
    @Transactional
    void loanCreationTest() {
        Loan foundLoan = entityManager.find(Loan.class, loan.getId());

        assertThat(foundLoan).isNotNull();
        assertThat(foundLoan.getId()).isGreaterThan(0);
        assertThat(foundLoan.getUser()).isEqualTo(user);
        assertThat(foundLoan.getBook()).isEqualTo(book);
        assertThat(foundLoan.getBorrowDate()).isNotNull();
        assertThat(foundLoan.getReturnDate()).isNull();
    }

    @Test
    @Transactional
    void loanDeletionTest() {
        loan.getUser().getLoans().remove(loan);
        loan.setUser(null);

        loan.getBook().setLoan(null);
        loan.setBook(null);

        entityManager.remove(loan);
        entityManager.flush();
        entityManager.clear();

        Loan foundLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(foundLoan).isNull();
    }




    @Test
    @Transactional
    void loanUpdateTest() {
        LocalDateTime newReturnDate = LocalDateTime.now().plusDays(10);
        loan.setReturnDate(newReturnDate);
        entityManager.persist(loan);
        entityManager.flush();

        Loan foundLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(foundLoan.getReturnDate()).isEqualTo(newReturnDate);
    }

    @Test
    @Transactional
    void loanCascadeDeleteWithBookTest() {
        entityManager.remove(book);
        entityManager.flush();

        Loan foundLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(foundLoan).isNull();
    }

    @Test
    @Transactional
    void loanCascadeDeleteWithUserTest() {
        entityManager.remove(user);
        entityManager.flush();
        entityManager.clear();

        Loan foundLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(foundLoan).isNull();
    }
}
