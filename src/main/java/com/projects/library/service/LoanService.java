package com.projects.library.service;

import com.projects.library.dto.request.AddLoanRequest;
import com.projects.library.dto.response.LoanResponse;
import com.projects.library.enums.BookStatus;
import com.projects.library.exception.BookAlreadyRentedException;
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
        System.out.println("Book rental!");
        Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> new BookNotFoundException(request.bookId()));
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new UserNotFoundException(request.userId()));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookAlreadyRentedException(request.bookId());
        }

        Loan loan = new Loan(user, book, LocalDateTime.now());

        book.setLoan(loan);
        book.setStatus(BookStatus.RENTED);

        loanRepository.save(loan);
        bookRepository.save(book);

        return loanMapper.toLoanResponse(loan);
    }


    public void bookReturn(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        Loan loan = book.getLoan();
        loan.setReturnDate(LocalDateTime.now());
        book.setStatus(BookStatus.AVAILABLE);

        loanRepository.save(loan);
        bookRepository.save(book);
    }


    public void deleteLoan(long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException(id));
        loanRepository.delete(loan);
    }
}
