package com.projects.library.repository;

import com.projects.library.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT count(b) FROM Book b WHERE b.status = :status")
    int countByStatus(@Param("status") String status);
    Book findByTitleAndStatus(Title title, String status);

    Book findByStatus(String status);
}
