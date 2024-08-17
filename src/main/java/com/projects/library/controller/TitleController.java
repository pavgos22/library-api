package com.projects.library.controller;

import com.projects.library.dto.request.AddTitleRequest;
import com.projects.library.dto.response.TitleResponse;
import com.projects.library.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/titles")
@RequiredArgsConstructor
public class TitleController {

    private final TitleService titleService;

    @GetMapping
    public ResponseEntity<List<TitleResponse>> getAllTitles() {
        List<TitleResponse> titles = titleService.getAllTitles();
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TitleResponse> getTitleById(@PathVariable Long id) {
        TitleResponse title = titleService.getTitle(id);
        return new ResponseEntity<>(title, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TitleResponse> addTitle(@RequestBody AddTitleRequest addTitleRequest) {
        TitleResponse createdTitle = titleService.addTitle(addTitleRequest);
        return new ResponseEntity<>(createdTitle, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
        titleService.deleteTitle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}