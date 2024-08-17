package com.projects.library.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String title) {
        super("Book '" + title + "' is not available for loan.");
    }
}
