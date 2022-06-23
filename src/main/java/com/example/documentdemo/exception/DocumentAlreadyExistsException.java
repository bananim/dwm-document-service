package com.example.documentdemo.exception;
public class DocumentAlreadyExistsException extends RuntimeException {
    private String message;

    public DocumentAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

    public DocumentAlreadyExistsException() {
    }
}