package com.example.documentdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.documentdemo.dto.DocumentMetaData;
import com.example.documentdemo.exception.DocumentAlreadyExistsException;
import com.example.documentdemo.exception.FileStorageException;
import com.example.documentdemo.model.Document;
import com.example.documentdemo.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(description = "Document service that manages storage and retrieval\r\n"
    + "of documents for users", name = "Document Service")
@Slf4j
@RestController
@RequestMapping("/api/document")
public class DocumentController {

  @Autowired
  private DocumentService documentService;

  // =====add document api start====
  @Operation(summary = "Upload documents", description = "Upload one or more documents for a user")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
      @ApiResponse(responseCode = "400", description = "${api.response-codes.badRequest.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }),
      @ApiResponse(responseCode = "404", description = "${api.response-codes.notFound.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }) })
  @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
  public ResponseEntity<?> uploadDocuments(@RequestParam(value = "documents") MultipartFile[] multipartFiles,
      @RequestParam(value = "username") String userName) throws FileStorageException, DocumentAlreadyExistsException {
    log.info("Document upload begins for user {}", userName);
    documentService.addDocuments(multipartFiles, userName);
    log.info("Document successfully uploaded for user {}", userName);
    return new ResponseEntity<>("Add Successful", HttpStatus.CREATED);

  }

  // =======Download api start======
  @Operation(summary = "Download document", description = "Download document for a given name and username")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
      @ApiResponse(responseCode = "400", description = "${api.response-codes.badRequest.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }),
      @ApiResponse(responseCode = "404", description = "${api.response-codes.notFound.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }) })
  @GetMapping("/downloadFile")
  public ResponseEntity<?> downloadFile(@RequestParam(value = "filename") String fileName,
      @RequestParam(value = "username") String userName) {
    Resource resource = null;
    log.info("Document download begins for user {}", userName);
    resource = documentService.getResourceByUserAndDocumentName(userName, fileName);

    String contentType = "application/octet-stream";
    String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
    log.info("Document download success for user {}", userName);
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
  }

  // ======Get All file names for a user api========
  @Operation(summary = "Get documents", description = "Get list of documents for a user")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
      @ApiResponse(responseCode = "400", description = "${api.response-codes.badRequest.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }),
      @ApiResponse(responseCode = "404", description = "${api.response-codes.notFound.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }) })
  @GetMapping(path = "/documents")
  public ResponseEntity<?> getDocumentsByUserName(@RequestParam(value = "username") String userName) {
    List<DocumentMetaData> docMetaDataList = documentService.getDocumentsByUserName(userName);
    return new ResponseEntity<>((docMetaDataList), HttpStatus.OK);
  }

  // =======Update Document meta data api ==========
  @Operation(summary = "Update Document", description = "Update document name for a user")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
      @ApiResponse(responseCode = "400", description = "${api.response-codes.badRequest.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }),
      @ApiResponse(responseCode = "404", description = "${api.response-codes.notFound.desc}", content = {
          @Content(examples = { @ExampleObject(value = "") }) }) })
  @PutMapping(path = "/update/{id}", consumes = "multipart/form-data")
  public ResponseEntity<?> updateDocument(@RequestParam(value = "name", required = false) String newName,
      @PathVariable Long id, @RequestParam(value = "document", required = false) MultipartFile multipartFile) {
    Document updatedDoc = documentService.updateDocument(newName, id, multipartFile);
    return new ResponseEntity<>(updatedDoc, HttpStatus.OK);
  }
}