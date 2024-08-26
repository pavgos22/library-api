package com.projects.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.library.dto.request.AddUserRequest;
import com.projects.library.dto.response.BookResponse;
import com.projects.library.dto.response.UserResponse;
import com.projects.library.enums.BookStatus;
import com.projects.library.service.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse(1L, "Joe", "Nemo", LocalDateTime.now());
        bookResponse = new BookResponse(1L, "Test Title", BookStatus.AVAILABLE);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        List<UserResponse> users = Collections.singletonList(userResponse);
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Joe")))
                .andExpect(jsonPath("$[0].lastName", is("Nemo")));
    }

    @Test
    void shouldGetUserById() throws Exception {
        Mockito.when(userService.getUser(anyLong())).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Joe")))
                .andExpect(jsonPath("$.lastName", is("Nemo")));
    }

    @Test
    void shouldGetBooksLoanedByUser() throws Exception {
        List<BookResponse> books = Collections.singletonList(bookResponse);
        Mockito.when(userService.getBooksLoanedByUser(anyLong())).thenReturn(books);

        mockMvc.perform(get("/api/users/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Title")));
    }

    @Test
    void shouldAddUser() throws Exception {
        AddUserRequest addUserRequest = new AddUserRequest("Adam", "Morgan");
        Mockito.when(userService.addUser(any(AddUserRequest.class))).thenReturn(new UserResponse(2L, "Adam", "Morgan", LocalDateTime.now()));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(2)))
                .andExpect(jsonPath("$.firstName", is("Adam")))
                .andExpect(jsonPath("$.lastName", is("Morgan")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
