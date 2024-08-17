package com.projects.library.controller;

import com.projects.library.dto.request.AddTitleRequest;
import com.projects.library.model.Title;
import com.projects.library.services.TitleService;
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
    public ResponseEntity<List<Title>> getAllTitles() {
        List<Title> titles = titleService.getAllTitles();
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Title> getTitleById(@PathVariable Long id) {
        Title title = titleService.getTitle(id);
        return new ResponseEntity<>(title, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Title> addTitle(@RequestBody AddTitleRequest addTitleRequest) {
        Title createdTitle = titleService.addTitle(addTitleRequest);
        return new ResponseEntity<>(createdTitle, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
        titleService.deleteTitle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}