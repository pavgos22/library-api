package com.projects.library.services;

import com.projects.library.model.Title;
import com.projects.library.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleService {
    private TitleRepository repository;

    public List<Title> getAllTitles() {
        return repository.findAll();
    }

    public Title saveTitle(final Title title) {
        return repository.save(title);
    }

    public void deleteTitle(Title title) {
        repository.delete(title);
    }
}
