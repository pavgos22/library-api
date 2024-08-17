package com.projects.library.repository;

import com.projects.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

}
