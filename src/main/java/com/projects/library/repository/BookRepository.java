package com.projects.library.repository;

import com.projects.library.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT count(b) FROM Book b WHERE b.status = :status")
    int countByStatus(@Param("status") BookStatus status);
    List<Book> findByStatus(BookStatus status);
    List<Book> findByStatusAndTitleId(BookStatus status, Long titleId);
}
