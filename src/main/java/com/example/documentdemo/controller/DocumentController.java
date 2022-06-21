package com.example.documentdemo.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.documentdemo.dto.DocumentMetaData;
import com.example.documentdemo.model.Document;
import com.example.documentdemo.service.DocumentService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);

  @Autowired
  private DocumentService documentService;

  @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "multipart/form-data")
  public ResponseEntity<?> addDocument(@RequestParam(value = "documents") MultipartFile[] multipartFiles,
      @RequestParam(value = "username") String userName) {
    LOG.debug("Adding document >>>");
    try {
      documentService.addDocuments(multipartFiles, userName);

    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    } catch (Exception ex) {
      return new ResponseEntity<>("Can not save document", HttpStatus.BAD_REQUEST);
    }

    LOG.debug("<<< Document added");
    return new ResponseEntity<>("Add Successful", HttpStatus.OK);
  }

  // =======Download api start======
  @GetMapping("/downloadFile")
  public ResponseEntity<?> downloadFile(@RequestParam(value = "filename") String fileName,
      @RequestParam(value = "username") String userName) {
    Resource resource = null;
    try {
      resource = documentService.getResourceByUserAndDocumentName(userName, fileName);
      if (resource == null) {
        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
      }
      String contentType = "application/octet-stream";
      String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
      return ResponseEntity //
          .ok()//
          .contentType(MediaType.parseMediaType(contentType))//
          .header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
    } catch (IOException excp) {
      return ResponseEntity.internalServerError().build();
    }

  }

  // ======Get All file names for a user api========
  @GetMapping(path = "/documents")
  public ResponseEntity<?> getDocumentsByUserName(@RequestParam(value = "username") String userName) {
    try {
      List<DocumentMetaData> docMetaDataList = documentService.getDocumentsByUserName(userName);
      if (docMetaDataList.isEmpty()) {
        return new ResponseEntity<>("No documents found", HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>((docMetaDataList), HttpStatus.OK);
    } catch (Exception excp) {
      return ResponseEntity.internalServerError().build();
    }

  }

  // =======Update Document meta data api ==========
  @PutMapping(path = "/update/{id}")
  public ResponseEntity<?> updateDocumentName(@RequestParam(value = "name") String newName, @PathVariable Long id) {
    try {
      Document updatedDoc = documentService.updateDocument(newName, id);
      return new ResponseEntity<>(updatedDoc, HttpStatus.OK);
    } catch (NoSuchElementException ex) {
      return new ResponseEntity<>("Document Not Found", HttpStatus.BAD_REQUEST);
    }

  }
}