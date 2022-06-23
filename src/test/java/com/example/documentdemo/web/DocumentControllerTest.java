package com.example.documentdemo.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import com.example.documentdemo.IntegrationTest;
import com.example.documentdemo.model.Document;
import com.example.documentdemo.property.DocumentStorageProperty;
import com.example.documentdemo.repository.DocumentRepository;

public class DocumentControllerTest extends IntegrationTest {

  @Autowired
  DocumentRepository docRepo;

  @Autowired
  DocumentStorageProperty documentStorageProperty;

   void prepareTestData() throws Exception {
    uploadDocument();
  }

  @AfterEach
  public void tearDown() throws IOException {
    List<Document> documentList = (List<Document>) docRepo.findByUserName("test_user");
    if (documentList != null && !documentList.isEmpty()) {
      docRepo.deleteAll(documentList);
    }
    String folderRootPath = documentStorageProperty.getUploadDirectory();
    Path docStorageLocation = Paths.get(folderRootPath, "test_user").toAbsolutePath().normalize();
    FileUtils.deleteDirectory(new File(docStorageLocation.toString()));

  }

  @Test
  void uploadDocument() throws Exception {
    MockMultipartFile firstFile = new MockMultipartFile("documents", "filename.txt", "text/plain",
        "some xml".getBytes());
    MockMultipartFile secondFile = new MockMultipartFile("documents", "other-file-name.data", "text/plain",
        "some other type".getBytes());
    MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json",
        "{\"json\": \"someValue\"}".getBytes());

    mvc.perform(multipart("/api/document/upload").file(firstFile).file(secondFile).file(jsonFile).param("username",
        "test_user")).andExpect(status().isCreated());
  }

  @Test
  void getDocumentsByUserName() throws Exception {
    prepareTestData();
    mvc.perform(get("/api/document/documents").contentType("application/json").param("username", "test_user"))
        .andExpect(status().isOk());
  }

  @Test
  void downloadFile() throws Exception {
    prepareTestData();
    mvc.perform(get("/api/document/downloadFile").contentType("application/octet-stream").param("username", "test_user")
        .param("filename", "filename.txt")).andExpect(status().isOk());
  }

  @Test
  void updateDocument() throws Exception {
    prepareTestData();
    List<Document> documentList = (List<Document>) docRepo.findByUserName("test_user");
    if (documentList != null && !documentList.isEmpty()) {
      mvc.perform(put("/api/document//update/" + documentList.get(0).getId()).contentType("multipart/form-data")
          .param("name", "changedfile.txt")).andExpect(status().isOk());
    }

  }
}
