package com.projects.library.mapper;

import com.projects.library.dto.response.TitleResponse;
import com.projects.library.model.Title;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TitleMapper {

    private final BookMapper bookMapper;

    public TitleMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public TitleResponse toTitleResponse(Title title) {
        if (title == null) {
            return null;
        }
        return new TitleResponse(
                title.getId(),
                title.getTitle(),
                title.getAuthor(),
                title.getYear(),
                title.getBooks().stream()
                        .map(bookMapper::toBookResponse)
                        .collect(Collectors.toSet())
        );
    }

    public Set<TitleResponse> mapToTitleResponseSet(Set<Title> titles) {
        return titles.stream()
                .map(this::toTitleResponse)
                .collect(Collectors.toSet());
    }
}
