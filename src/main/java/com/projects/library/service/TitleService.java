package com.projects.library.service;

import com.projects.library.dto.request.AddTitleRequest;
import com.projects.library.dto.response.TitleResponse;
import com.projects.library.exception.TitleDeletionException;
import com.projects.library.exception.TitleNotFoundException;
import com.projects.library.mapper.TitleMapper;
import com.projects.library.model.Title;
import com.projects.library.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TitleService {
    private final TitleRepository repository;
    private final TitleMapper titleMapper;

    public List<TitleResponse> getAllTitles() {
        List<Title> titles = repository.findAll();
        return titles.stream()
                .map(titleMapper::toTitleResponse)
                .collect(Collectors.toList());
    }

    public TitleResponse addTitle(AddTitleRequest addTitleRequest) {
        Title title = new Title(addTitleRequest.title(), addTitleRequest.author(), addTitleRequest.year());
        Title savedTitle = repository.save(title);
        return titleMapper.toTitleResponse(savedTitle);
    }

    public void deleteTitle(long id) {
        Title title = repository.findById(id).orElseThrow(() -> new TitleNotFoundException(id));
        try {
            repository.delete(title);
        } catch (Exception ex) {
            throw new TitleDeletionException(id);
        }
    }

    public TitleResponse getTitle(Long id) {
        Title title = repository.findById(id).orElseThrow(() -> new TitleNotFoundException(id));
        return titleMapper.toTitleResponse(title);
    }
}
