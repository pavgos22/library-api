package com.projects.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.library.dto.request.AddLoanRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.dto.response.LoanResponse;
import com.projects.library.dto.response.UserResponse;
import com.projects.library.enums.BookStatus;
import com.projects.library.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    private LoanResponse loanResponse;

    @BeforeEach
    void setUp() {
        UserResponse userResponse = new UserResponse(1L, "Joe", "Nemo", LocalDateTime.now());
        BookResponse bookResponse = new BookResponse(1L, "Test Title", BookStatus.AVAILABLE);

        loanResponse = new LoanResponse(1L, userResponse, bookResponse, LocalDateTime.now(), null);
    }


    @Test
    void shouldGetAllLoans() throws Exception {
        List<LoanResponse> loans = Collections.singletonList(loanResponse);
        Mockito.when(loanService.getAllLoans()).thenReturn(loans);

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].book.id", is(1)))
                .andExpect(jsonPath("$[0].user.userId", is(1)));
    }


    @Test
    void shouldGetLoanById() throws Exception {
        Mockito.when(loanService.getLoan(anyLong())).thenReturn(loanResponse);

        mockMvc.perform(get("/api/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.book.id", is(1)))
                .andExpect(jsonPath("$.user.userId", is(1)));
    }


    @Test
    void shouldRentBook() throws Exception {
        AddLoanRequest request = new AddLoanRequest(1L, 1L);
        Mockito.when(loanService.rentBook(any(AddLoanRequest.class))).thenReturn(loanResponse);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.book.id", is(1)))
                .andExpect(jsonPath("$.user.userId", is(1)));
    }


    @Test
    void shouldReturnBook() throws Exception {
        mockMvc.perform(put("/api/loans/1/return"))
                .andExpect(status().isOk());

        Mockito.verify(loanService, Mockito.times(1)).bookReturn(1L);
    }

    @Test
    void shouldDeleteLoan() throws Exception {
        mockMvc.perform(delete("/api/loans/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(loanService, Mockito.times(1)).deleteLoan(1L);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
