package com.projects.library.repository;

import com.projects.library.enums.BookStatus;
import com.projects.library.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT count(b) FROM Book b WHERE b.status = :status")
    int countByStatus(@Param("status") BookStatus status);
    Book findByTitleAndStatus(Title title, BookStatus status);

    Book findByStatus(BookStatus status);
}
