package com.example.documentdemo.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class FileDownloadUtil {
  private Path foundFile;

  public Resource getFileAsResource(String fileCode, Path dirPath) throws IOException {
    Files.list(dirPath).forEach(file -> {
      if (file.getFileName().toString().startsWith(fileCode)) {
        foundFile = file;
        return;
      }
    });

    if (foundFile != null) {
      return new UrlResource(foundFile.toUri());
    }

    return null;
  }
}