package com.projects.library.mapper;

import com.projects.library.dto.response.LoanResponse;
import com.projects.library.model.Loan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanMapper {

    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public LoanMapper(UserMapper userMapper, BookMapper bookMapper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public LoanResponse toLoanResponse(Loan loan) {
        if (loan == null) {
            return null;
        }
        return new LoanResponse(
                loan.getId(),
                userMapper.toUserResponse(loan.getUser()),
                bookMapper.toBookResponse(loan.getBook()),
                loan.getBorrowDate(),
                loan.getReturnDate()
        );
    }

    public List<LoanResponse> mapToLoanResponseList(List<Loan> loans) {
        return loans.stream()
                .map(this::toLoanResponse)
                .collect(Collectors.toList());
    }
}
