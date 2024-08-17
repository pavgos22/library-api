package com.projects.library.exception;

public class TitleDeletionException extends RuntimeException {
    public TitleDeletionException(Long id) {
        super("Title with ID " + id + " cannot be deleted.");
    }
}
