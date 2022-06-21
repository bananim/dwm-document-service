package com.example.documentdemo.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.documentdemo.dto.DocumentMetaData;
import com.example.documentdemo.model.Document;
import com.example.documentdemo.property.DocumentStorageProperty;
import com.example.documentdemo.repository.DocumentRepository;
import com.example.documentdemo.service.DocumentService;
import com.example.documentdemo.util.FileDownloadUtil;

@Service
public class DocumentServiceImpl implements DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  private  Path docStorageLocation;
  
  private final String folderRootPath;

  @Autowired
  public DocumentServiceImpl(DocumentStorageProperty documentStorageProperty) throws IOException {
    folderRootPath = documentStorageProperty.getUploadDirectory();
}

  @Override
  @Transactional
  public void addDocuments(MultipartFile[] multipartFiles, String userName)
      throws NoSuchAlgorithmException, IOException {
    for (MultipartFile multipartFile : multipartFiles) {
      create(multipartFile, userName);
    }
  }

  private void create(MultipartFile multipartFile, String userName) throws NoSuchAlgorithmException, IOException {
    Document document = new Document();
    document.setUserName(userName);
    document.setName(multipartFile.getOriginalFilename());
    document.setMimeType(multipartFile.getContentType());
    document.setSize(multipartFile.getSize());
    //document.setHash();
    storeDocument(multipartFile, document.getName(), document.getUserName());
    documentRepository.save(document);
  }

  private void storeDocument(MultipartFile file, String name, String userName) throws IOException {
    this.docStorageLocation = Paths.get(folderRootPath, userName).toAbsolutePath().normalize();
    Files.createDirectories(this.docStorageLocation);
    Path targetLocation = this.docStorageLocation.resolve(name);    
    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  @Transactional
  public Document updateDocument(String newName, Long id) {
    Optional<Document> oldDoc = documentRepository.findById(id);

    return oldDoc.map(document -> {
      try {
        //String oldHash = document.getHash();
        this.docStorageLocation = Paths.get(folderRootPath, document.getUserName()).toAbsolutePath().normalize();
        Path targetLocation = this.docStorageLocation.resolve(document.getName());
        if (newName != null) {
          document.setName(newName);
        }
        //document.setHash();
        Files.move(targetLocation, targetLocation.resolveSibling(document.getName()));
      } catch (IOException e) {
        e.printStackTrace();
      }
      return documentRepository.save(document);

    }).orElseThrow();
  }

  @Override
  public List<DocumentMetaData> getDocumentsByUserName(String userName) {
    List<Document> userDocuments = (List<Document>) documentRepository.findByUserName(userName);
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
  public Resource getResourceByUserAndDocumentName(String userName, String fileName) throws IOException {
    Document document = documentRepository.findByNameAndUserName(fileName, userName);
    if (document != null) {
      //String fileCode = document.getHash();
      this.docStorageLocation = Paths.get(folderRootPath, document.getUserName()).toAbsolutePath().normalize();
      FileDownloadUtil downloadUtil = new FileDownloadUtil();
      Resource resource = downloadUtil.getFileAsResource(document.getName(), this.docStorageLocation);
      return resource;
    } else {
      return null;
    }

  }
}