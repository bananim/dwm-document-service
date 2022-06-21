package com.example.documentdemo.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.documentdemo.dto.DocumentMetaData;
import com.example.documentdemo.model.Document;

public interface DocumentService {

	/**
	 * Add an array of files
	 * 
	 * @param multipartFiles
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public void addDocuments(MultipartFile[] multipartFiles, String userName) throws NoSuchAlgorithmException, IOException;
	public Document updateDocument(String newName, Long id) throws NoSuchElementException;
	public List<DocumentMetaData> getDocumentsByUserName(String userName);
	public Resource getResourceByUserAndDocumentName(String userName, String fileName) throws IOException;
}