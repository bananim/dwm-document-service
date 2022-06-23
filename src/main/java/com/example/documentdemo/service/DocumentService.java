package com.example.documentdemo.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MultipartFile;

import com.example.documentdemo.dto.DocumentMetaData;
import com.example.documentdemo.exception.DocumentAlreadyExistsException;
import com.example.documentdemo.exception.DocumentNotFoundException;
import com.example.documentdemo.exception.FileStorageException;
import com.example.documentdemo.model.Document;

public interface DocumentService {

  public void addDocuments(MultipartFile[] multipartFiles, String userName)
      throws FileStorageException, DocumentAlreadyExistsException;

  public Document updateDocument(String newName, Long id, MultipartFile fileToUpdate)
      throws FileStorageException, DocumentNotFoundException, MissingServletRequestParameterException;

  public List<DocumentMetaData> getDocumentsByUserName(String userName);

  public Resource getResourceByUserAndDocumentName(String userName, String fileName) throws DocumentNotFoundException;
}