package com.example.documentdemo.exception;

public class DocumentNotFoundException extends RuntimeException {
  private String message;

  public DocumentNotFoundException(String message) {
      super(message);
      this.message = message;
  }

  public DocumentNotFoundException() {
  }
}