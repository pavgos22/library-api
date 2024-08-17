package com.projects.library.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(long id) {
        super("Book with ID " + id + " not found.");
    }
}
