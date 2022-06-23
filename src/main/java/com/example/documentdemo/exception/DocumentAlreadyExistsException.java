package com.example.documentdemo.exception;

public class DocumentAlreadyExistsException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String message;

  public DocumentAlreadyExistsException(String message) {
    super(message);
    this.message = message;
  }

  public DocumentAlreadyExistsException() {
  }
}