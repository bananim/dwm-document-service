package com.example.documentdemo.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
  @Value(value = "${data.exception.message1}")
  private String message1;
  @Value(value = "${data.exception.message2}")
  private String message2;
  @Value(value = "${data.exception.message3}")
  private String message3;
  @Value(value = "${data.exception.message4}")
  private String message4;

  @ExceptionHandler(value = DocumentAlreadyExistsException.class)
  public ResponseEntity<String> documentAlreadyExistsException(
      DocumentAlreadyExistsException documentAlreadyExistsException) {
    return new ResponseEntity<String>(message1, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = DocumentNotFoundException.class)
  public ResponseEntity documentNotFoundException(DocumentNotFoundException documentNotFoundException) {
    return new ResponseEntity<String>(message2, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Object> databaseConnectionFailsException(Exception exception) {
    return new ResponseEntity<>(message3, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = FileStorageException.class)
  public ResponseEntity fileStorageException(FileStorageException fileStorageException) {
    return new ResponseEntity<String>(message4, HttpStatus.EXPECTATION_FAILED);
  }
  
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity handleMaxSizeException(MaxUploadSizeExceededException maxUploadSizeExceededException) {
      return new ResponseEntity<String>("Maximum file size limit exceeded, allowed limit is 2MB", HttpStatus.BAD_REQUEST);
  }
}