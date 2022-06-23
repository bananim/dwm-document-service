package com.example.documentdemo.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class GlobalExceptionHandler {
  @Value(value = "${data.exception.message.doc.exists}")
  private String messageDocExists;
  @Value(value = "${data.exception.message.doc.not.found}")
  private String messageNotFound;
  @Value(value = "${data.exception.message.conn.lost}")
  private String messageConnLost;
  @Value(value = "${data.exception.message.storage.failed}")
  private String messageStorageFailed;

  @ExceptionHandler(value = DocumentAlreadyExistsException.class)
  public ResponseEntity<String> documentAlreadyExistsException(
      DocumentAlreadyExistsException documentAlreadyExistsException) {
    return new ResponseEntity<String>(messageDocExists, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  public ResponseEntity<String> documentNotFoundException(DocumentNotFoundException documentNotFoundException) {
    return new ResponseEntity<String>(messageNotFound, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = FileStorageException.class)
  public ResponseEntity<String> fileStorageException(FileStorageException fileStorageException) {
    return new ResponseEntity<String>(messageStorageFailed, HttpStatus.EXPECTATION_FAILED);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException maxUploadSizeExceededException) {
    return new ResponseEntity<String>("Maximum file size limit exceeded, allowed limit is 2MB", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ MissingServletRequestParameterException.class, MissingServletRequestPartException.class,
      HttpMediaTypeNotSupportedException.class })
  public ResponseEntity<String> handleMissingInput(
      MissingServletRequestParameterException missingServletRequestParameterException) {
    return new ResponseEntity<String>("Mandatory parameter missing", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> notFoundException(EntityNotFoundException entityNotFoundException) {
    return new ResponseEntity<String>(messageNotFound, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Object> databaseConnectionFailsException(Exception exception) {
    return new ResponseEntity<>(messageConnLost, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}