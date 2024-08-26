package com.projects.library.exception;

public class BookAlreadyRentedException extends RuntimeException {
    public BookAlreadyRentedException(Long id) {
        super("Book '" + id + "' already rented.");
    }
}
