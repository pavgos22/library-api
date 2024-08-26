package com.projects.library.service;

import com.projects.library.dto.request.AddTitleRequest;
import com.projects.library.dto.response.TitleResponse;
import com.projects.library.exception.TitleNotFoundException;
import com.projects.library.model.Title;
import com.projects.library.repository.TitleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TitleServiceTestSuite {

    @Autowired
    private TitleService titleService;

    @Autowired
    private TitleRepository titleRepository;

    private Title title;

    @BeforeEach
    void setUp() {
        titleRepository.deleteAll();

        title = new Title("Test Title", "Test Author", 2024);
        titleRepository.save(title);
    }

    @Test
    void shouldGetAllTitles() {
        List<TitleResponse> titles = titleService.getAllTitles();

        assertThat(titles).hasSize(1);
        assertThat(titles.get(0).id()).isEqualTo(title.getId());
    }

    @Test
    void shouldAddTitle() {
        AddTitleRequest request = new AddTitleRequest("New Title", "New Author", 2024);
        TitleResponse newTitle = titleService.addTitle(request);

        assertThat(newTitle).isNotNull();
        assertThat(newTitle.title()).isEqualTo(request.title());
        assertThat(newTitle.author()).isEqualTo(request.author());
        assertThat(newTitle.year()).isEqualTo(request.year());
    }

    @Test
    void shouldGetTitleById() {
        TitleResponse foundTitle = titleService.getTitle(title.getId());

        assertThat(foundTitle).isNotNull();
        assertThat(foundTitle.id()).isEqualTo(title.getId());
    }

    @Test
    void shouldThrowTitleNotFoundExceptionWhenTitleNotFound() {
        assertThrows(TitleNotFoundException.class, () -> titleService.getTitle(999L));
    }

    @Test
    void shouldDeleteTitle() {
        titleService.deleteTitle(title.getId());

        assertThrows(TitleNotFoundException.class, () -> titleService.getTitle(title.getId()));
        assertThat(titleRepository.findAll()).isEmpty();
    }
}
