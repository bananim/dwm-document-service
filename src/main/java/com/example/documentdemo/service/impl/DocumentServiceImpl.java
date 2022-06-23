package com.example.documentdemo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MultipartFile;

import com.example.documentdemo.dto.DocumentMetaData;
import com.example.documentdemo.exception.DocumentAlreadyExistsException;
import com.example.documentdemo.exception.DocumentNotFoundException;
import com.example.documentdemo.exception.FileStorageException;
import com.example.documentdemo.model.Document;
import com.example.documentdemo.property.DocumentStorageProperty;
import com.example.documentdemo.repository.DocumentRepository;
import com.example.documentdemo.service.DocumentService;
import com.example.documentdemo.util.FileDownloadUtil;

@Service
public class DocumentServiceImpl implements DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  private Path docStorageLocation;

  private final String folderRootPath;

  @Autowired
  public DocumentServiceImpl(DocumentStorageProperty documentStorageProperty) throws IOException {
    folderRootPath = documentStorageProperty.getUploadDirectory();
  }

  @Override
  @Transactional
  public void addDocuments(MultipartFile[] multipartFiles, String userName) {
    for (MultipartFile multipartFile : multipartFiles) {
      create(multipartFile, userName);
    }
  }

  private void create(MultipartFile multipartFile, String userName) {
    Document document = new Document();
    document.setUserName(userName);
    document.setName(multipartFile.getOriginalFilename());
    document.setMimeType(multipartFile.getContentType());
    document.setSize(multipartFile.getSize());
    document.setCreatedTime(new Date());
    if (documentRepository.findByNameAndUserName(document.getName(), document.getUserName()) != null) {
      throw new DocumentAlreadyExistsException();
    }
    documentRepository.save(document);
    storeDocument(multipartFile, document.getName(), document.getUserName());
  }

  private void storeDocument(MultipartFile file, String name, String userName) {
    try {
      this.docStorageLocation = Paths.get(folderRootPath, userName).toAbsolutePath().normalize();

      Files.createDirectories(this.docStorageLocation);
      Path targetLocation = this.docStorageLocation.resolve(name);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    } catch (InvalidPathException | IOException ex) {
      throw new FileStorageException();
    }
  }

  @Override
  @Transactional
  public Document updateDocument(String newName, Long id, MultipartFile fileToUpdate) throws MissingServletRequestParameterException {
    if(newName == null && fileToUpdate == null) {
      throw new MissingServletRequestParameterException(newName, newName);
    }
    Optional<Document> oldDoc = Optional.of(documentRepository.getOne(id));
    
    return oldDoc.map(document -> {
      try {
        // String oldHash = document.getHash();
        this.docStorageLocation = Paths.get(folderRootPath, document.getUserName()).toAbsolutePath().normalize();
        Path targetLocation = this.docStorageLocation.resolve(document.getName());
        if (newName != null && fileToUpdate == null) {
          document.setName(newName);
          document.setUpdatedTime(new Date());
          document = documentRepository.save(document);
          Files.move(targetLocation, targetLocation.resolveSibling(document.getName()));
        }
        if (fileToUpdate != null) {
          document.setName(fileToUpdate.getOriginalFilename());
          document.setMimeType(fileToUpdate.getContentType());
          document.setUpdatedTime(new Date());
          document.setSize(fileToUpdate.getSize());
          document = documentRepository.save(document);
          storeDocument(fileToUpdate, document.getName(), document.getUserName());
        }

      } catch (InvalidPathException | IOException ex) {
        throw new FileStorageException();
      }
      return document;

    }).orElseThrow(DocumentNotFoundException::new);

  }

  @Override
  public List<DocumentMetaData> getDocumentsByUserName(String userName) {
    List<Document> userDocuments = (List<Document>) documentRepository.findByUserName(userName);
    if (userDocuments == null || userDocuments.isEmpty()) {
      throw new DocumentNotFoundException();
    }
    List<DocumentMetaData> docMetaDataList = new ArrayList<>();
    for (Document document : userDocuments) {
      DocumentMetaData docMeta = new DocumentMetaData();
      docMeta.setId(document.getId());
      docMeta.setName(document.getName());
      docMeta.setType(document.getMimeType());
      docMeta.setSize(document.getSize());
      docMetaDataList.add(docMeta);
    }
    return docMetaDataList;
  }

  @Override
  public Resource getResourceByUserAndDocumentName(String userName, String fileName) {
    Document document = documentRepository.findByNameAndUserName(fileName, userName);
    Resource resource = null;
    if (document != null) {
      // String fileCode = document.getHash();
      this.docStorageLocation = Paths.get(folderRootPath, document.getUserName()).toAbsolutePath().normalize();
      FileDownloadUtil downloadUtil = new FileDownloadUtil();
      try {
        resource = downloadUtil.getFileAsResource(document.getName(), this.docStorageLocation);
      } catch (IOException e) {
        throw new DocumentNotFoundException();
      }
    }
    if (resource == null) {
      throw new DocumentNotFoundException();
    }
    return resource;
  }
}