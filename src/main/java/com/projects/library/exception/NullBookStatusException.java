package com.projects.library.exception;

public class NullBookStatusException extends RuntimeException {
    public NullBookStatusException() {
        super("Book status cannot be null");
    }
}
