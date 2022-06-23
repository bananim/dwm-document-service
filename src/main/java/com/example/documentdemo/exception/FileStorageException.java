package com.example.documentdemo.exception;

public class FileStorageException extends RuntimeException {
  private String message;

  public FileStorageException(String message) {
    super();
    this.message = message;
  }

  public FileStorageException() {
  }
}