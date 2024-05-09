package com.projects.library.model;

import com.projects.library.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookRepositoryTestSuite {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Transactional
    public void testCreateAndFindBook() {
        Title title = new Title("Effective Java", "Joshua Bloch", 2008);
        entityManager.persist(title);
        entityManager.flush();

        Book book = new Book();
        book.setTitle(title);
        book.setStatus("Available");
        Book savedBook = bookRepository.saveAndFlush(book);

        Book foundBook = bookRepository.findById(savedBook.getId()).orElseThrow();
        assertThat(foundBook.getTitle().getTitle()).isEqualTo("Effective Java");
    }
}
