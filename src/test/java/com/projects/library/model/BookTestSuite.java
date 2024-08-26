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
public class BookTestSuite {

    @PersistenceContext
    private EntityManager entityManager;

    private Title title;
    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        title = new Title("Test Title", "Test Author", 2023);
        entityManager.persist(title);

        user = new User("Joe", "Nemo", LocalDateTime.now());
        entityManager.persist(user);

        book = new Book(title, BookStatus.AVAILABLE);
        entityManager.persist(book);

        loan = new Loan(user, book, LocalDateTime.now());
        book.setLoan(loan);
        entityManager.persist(loan);

        entityManager.flush();
    }

    @Test
    @Transactional
    void bookCreationTest() {
        Book foundBook = entityManager.find(Book.class, book.getId());

        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getId()).isGreaterThan(0);
        assertThat(foundBook.getStatus()).isEqualTo(BookStatus.AVAILABLE);
        assertThat(foundBook.getTitle()).isEqualTo(title);
        assertThat(foundBook.getLoan()).isEqualTo(loan);
    }

    @Test
    @Transactional
    void loanRelationshipTest() {
        Loan foundLoan = entityManager.find(Loan.class, loan.getId());

        assertThat(foundLoan).isNotNull();
        assertThat(foundLoan.getBook()).isEqualTo(book);
        assertThat(foundLoan.getUser()).isEqualTo(user);
    }

    @Test
    @Transactional
    void bookLoanDeletionTest() {
        entityManager.remove(loan);
        entityManager.flush();

        entityManager.remove(book);
        entityManager.flush();

        Loan foundLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(foundLoan).isNull();
        Book foundBook = entityManager.find(Book.class, book.getId());
        assertThat(foundBook).isNull();
    }

    @Test
    @Transactional
    void titleAssociationIsolatedTest() {
        entityManager.remove(title);
        entityManager.flush();

        Title foundTitle = entityManager.find(Title.class, title.getId());
        assertThat(foundTitle).isNull();
    }

    @Test
    @Transactional
    void titleAssociationTest() {
        title.getBooks().forEach(book -> {
            if (book.getLoan() != null) {
                entityManager.remove(book.getLoan());
            }
            book.setTitle(null);
            entityManager.remove(book);
        });
        title.getBooks().clear();

        entityManager.flush();

        entityManager.remove(title);
        entityManager.flush();

        Title foundTitle = entityManager.find(Title.class, title.getId());
        assertThat(foundTitle).isNull();
    }
}
