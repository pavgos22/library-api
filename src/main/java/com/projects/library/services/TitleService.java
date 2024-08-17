package com.projects.library.services;

import com.projects.library.dto.request.AddTitleRequest;
import com.projects.library.exception.TitleDeletionException;
import com.projects.library.exception.TitleNotFoundException;
import com.projects.library.model.Title;
import com.projects.library.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleService {
    private final TitleRepository repository;
    private final TitleRepository titleRepository;

    public List<Title> getAllTitles() {
        return repository.findAll();
    }

    public Title addTitle(AddTitleRequest addTitleRequest) {
        Title title = new Title(addTitleRequest.title(), addTitleRequest.author(), addTitleRequest.year());
        return repository.save(title);
    }

    public void deleteTitle(long id) {
        Title title = titleRepository.findById(id).orElseThrow(() -> new TitleNotFoundException(id));
        repository.findById(id)
                .orElseThrow(() -> new TitleNotFoundException(id));

        try {
            repository.delete(title);
        } catch (Exception ex) {
            throw new TitleDeletionException(id);
        }
    }

    public Title getTitle(Long id) {
        return repository.findById(id).orElseThrow(() -> new TitleNotFoundException(id));
    }
}
