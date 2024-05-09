package com.projects.library.repository;

import com.projects.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;


public interface LoanRepository extends JpaRepository<Loan, Integer> {

}
