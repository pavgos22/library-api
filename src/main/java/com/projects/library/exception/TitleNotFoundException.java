package com.projects.library.exception;

public class TitleNotFoundException extends RuntimeException {
    public TitleNotFoundException(Long id) {
        super("Title with ID " + id + " not found.");
    }
}
