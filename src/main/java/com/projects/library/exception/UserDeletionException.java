package com.projects.library.exception;

public class UserDeletionException extends RuntimeException {
    public UserDeletionException(Long id) {
        super("User with ID " + id + " cannot be deleted.");
    }
}
