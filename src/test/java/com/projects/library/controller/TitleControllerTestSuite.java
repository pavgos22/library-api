package com.projects.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.library.dto.request.AddTitleRequest;
import com.projects.library.dto.response.TitleResponse;
import com.projects.library.service.TitleService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TitleController.class)
class TitleControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TitleService titleService;

    private TitleResponse titleResponse;

    @BeforeEach
    void setUp() {
        titleResponse = new TitleResponse(1L, "Test Title", "Test Author", 2024, null);
    }

    @Test
    void shouldGetAllTitles() throws Exception {
        List<TitleResponse> titles = Collections.singletonList(titleResponse);
        Mockito.when(titleService.getAllTitles()).thenReturn(titles);

        mockMvc.perform(get("/api/titles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Title")))
                .andExpect(jsonPath("$[0].author", is("Test Author")))
                .andExpect(jsonPath("$[0].year", is(2024)));
    }

    @Test
    void shouldGetTitleById() throws Exception {
        Mockito.when(titleService.getTitle(anyLong())).thenReturn(titleResponse);

        mockMvc.perform(get("/api/titles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.author", is("Test Author")))
                .andExpect(jsonPath("$.year", is(2024)));
    }

    @Test
    void shouldAddTitle() throws Exception {
        AddTitleRequest addTitleRequest = new AddTitleRequest("New Title", "New Author", 2024);
        TitleResponse createdTitleResponse = new TitleResponse(2L, "New Title", "New Author", 2024, null);

        Mockito.when(titleService.addTitle(any(AddTitleRequest.class))).thenReturn(createdTitleResponse);

        mockMvc.perform(post("/api/titles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addTitleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.author", is("New Author")))
                .andExpect(jsonPath("$.year", is(2024)));
    }

    @Test
    void shouldDeleteTitle() throws Exception {
        mockMvc.perform(delete("/api/titles/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(titleService, Mockito.times(1)).deleteTitle(1L);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
