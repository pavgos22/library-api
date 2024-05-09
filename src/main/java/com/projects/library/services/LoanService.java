package com.projects.library.services;
import com.projects.library.model.Book;
import com.projects.library.model.Loan;
import com.projects.library.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository repository;
    private final BookService bookService;

    public List<Loan> getAllLoans() {
        return repository.findAll();
    }

    @Transactional
    public Loan saveLoan(final Loan loan) {
        Book book = loan.getBook();
        bookService.changeStatus(book, "Rented");
        return repository.save(loan);
    }

    @Transactional
    public void bookReturn(Loan loan) {
        loan.setReturnDate(LocalDate.now());
        repository.save(loan);

        Book book = loan.getBook();
        bookService.changeStatus(book, "Available");
    }
}
