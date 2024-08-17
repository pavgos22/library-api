package com.projects.library.service;

import com.projects.library.dto.request.AddLoanRequest;
import com.projects.library.dto.response.LoanResponse;
import com.projects.library.enums.BookStatus;
import com.projects.library.exception.BookNotFoundException;
import com.projects.library.exception.LoanNotFoundException;
import com.projects.library.exception.UserNotFoundException;
import com.projects.library.mapper.LoanMapper;
import com.projects.library.model.Book;
import com.projects.library.model.Loan;
import com.projects.library.model.User;
import com.projects.library.repository.BookRepository;
import com.projects.library.repository.LoanRepository;
import com.projects.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;

    public List<LoanResponse> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loanMapper.mapToLoanResponseList(loans);
    }

    public LoanResponse getLoan(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException(id));
        return loanMapper.toLoanResponse(loan);
    }

    public LoanResponse rentBook(AddLoanRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new UserNotFoundException(request.userId()));
        Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> new BookNotFoundException(request.bookId()));

        book.setStatus(BookStatus.RENTED);
        bookRepository.save(book);

        Loan loan = new Loan(user, book, LocalDateTime.now());
        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toLoanResponse(savedLoan);
    }

    public void bookReturn(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        Loan loan = loanRepository.findById(book.getLoan().getId()).orElseThrow(() -> new LoanNotFoundException(book.getLoan().getId()));
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan);
    }

    public void deleteLoan(long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException(id));
        loanRepository.delete(loan);
    }
}
