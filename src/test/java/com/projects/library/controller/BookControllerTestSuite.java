package com.projects.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.library.dto.request.ChangeStatusRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.enums.BookStatus;
import com.projects.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        bookResponse = new BookResponse(1L, "Test Title", BookStatus.AVAILABLE);
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        List<BookResponse> books = Collections.singletonList(bookResponse);
        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Title")))
                .andExpect(jsonPath("$[0].status", is("AVAILABLE")));
    }

    @Test
    void shouldGetBookById() throws Exception {
        Mockito.when(bookService.getBook(anyLong())).thenReturn(bookResponse);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    @Test
    void shouldGetAvailableBooks() throws Exception {
        List<BookResponse> availableBooks = Collections.singletonList(bookResponse);
        Mockito.when(bookService.getAvailableBooks(anyLong())).thenReturn(availableBooks);

        mockMvc.perform(get("/api/books/available?titleId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Title")))
                .andExpect(jsonPath("$[0].status", is("AVAILABLE")));
    }

    @Test
    void shouldGetAvailableBooksCount() throws Exception {
        Mockito.when(bookService.getAvailableBooksCount()).thenReturn(5);

        mockMvc.perform(get("/api/books/available/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void shouldAddBook() throws Exception {
        Mockito.when(bookService.addBook(anyLong())).thenReturn(bookResponse);

        mockMvc.perform(post("/api/books/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService, Mockito.times(1)).deleteBook(1L);
    }

    @Test
    void shouldChangeBookStatus() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest(BookStatus.RENTED);

        mockMvc.perform(put("/api/books/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk());

        Mockito.verify(bookService, Mockito.times(1)).changeStatus(1L, request);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
