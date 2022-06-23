package com.example.documentdemo.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.documentdemo.dto.DocumentMetaData;
import com.example.documentdemo.exception.DocumentAlreadyExistsException;
import com.example.documentdemo.exception.DocumentNotFoundException;
import com.example.documentdemo.exception.FileStorageException;
import com.example.documentdemo.model.Document;

public interface DocumentService {

  /**
   * Add an array of files
   * 
   * @param multipartFiles
   * @throws IOException
   * @throws SQLException
   */
  public void addDocuments(MultipartFile[] multipartFiles, String userName)
      throws FileStorageException, DocumentAlreadyExistsException;

  public Document updateDocument(String newName, Long id, MultipartFile fileToUpdate)
      throws FileStorageException, DocumentNotFoundException;

  public List<DocumentMetaData> getDocumentsByUserName(String userName);

  public Resource getResourceByUserAndDocumentName(String userName, String fileName) throws DocumentNotFoundException;
}