package com.projects.library.model;

import com.projects.library.enums.BookStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TitleTestSuite {

    @PersistenceContext
    private EntityManager entityManager;

    private Title title;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        title = new Title("Test Title", "Test Author", 2024);
        entityManager.persist(title);

        book1 = new Book(title, BookStatus.AVAILABLE);
        book2 = new Book(title, BookStatus.RENTED);

        title.getBooks().add(book1);
        title.getBooks().add(book2);

        entityManager.persist(book1);
        entityManager.persist(book2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    void titleCreationTest() {
        Title foundTitle = entityManager.find(Title.class, title.getId());

        assertThat(foundTitle).isNotNull();
        assertThat(foundTitle.getId()).isGreaterThan(0);
        assertThat(foundTitle.getTitle()).isEqualTo("Test Title");
        assertThat(foundTitle.getAuthor()).isEqualTo("Test Author");
        assertThat(foundTitle.getYear()).isEqualTo(2024);

        assertThat(foundTitle.getBooks())
                .extracting(Book::getId)
                .containsExactlyInAnyOrder(book1.getId(), book2.getId());
    }


    @Test
    @Transactional
    void titleUpdateTest() {
        String newTitleName = "Updated Title";
        title.setTitle(newTitleName);
        title = entityManager.merge(title);
        entityManager.flush();

        Title foundTitle = entityManager.find(Title.class, title.getId());
        assertThat(foundTitle.getTitle()).isEqualTo(newTitleName);
    }


    @Test
    @Transactional
    void titleDeletionTest() {
        Title titleToDelete = entityManager.find(Title.class, title.getId());
        entityManager.remove(titleToDelete);
        entityManager.flush();
        entityManager.clear();

        Title foundTitle = entityManager.find(Title.class, title.getId());
        assertThat(foundTitle).isNull();

        Book foundBook1 = entityManager.find(Book.class, book1.getId());
        Book foundBook2 = entityManager.find(Book.class, book2.getId());
        assertThat(foundBook1).isNull();
        assertThat(foundBook2).isNull();
    }

    @Test
    @Transactional
    void bookDeletionWhenTitleIsDeletedTest() {
        Title titleToDelete = entityManager.find(Title.class, title.getId());
        entityManager.remove(titleToDelete);
        entityManager.flush();
        entityManager.clear();

        Book foundBook1 = entityManager.find(Book.class, book1.getId());
        Book foundBook2 = entityManager.find(Book.class, book2.getId());

        assertThat(foundBook1).isNull();
        assertThat(foundBook2).isNull();
    }


    @Test
    @Transactional
    void addingBooksToTitleTest() {
        Book newBook = new Book(title, BookStatus.AVAILABLE);
        entityManager.persist(newBook);
        entityManager.flush();

        Title foundTitle = entityManager.find(Title.class, title.getId());
        assertThat(foundTitle.getBooks()).contains(newBook);
    }
}
