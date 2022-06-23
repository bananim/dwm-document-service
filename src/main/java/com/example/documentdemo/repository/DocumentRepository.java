package com.example.documentdemo.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.documentdemo.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
  public Collection<Document> findByUserName(String userName);

  public Document findByNameAndUserName(String name, String userName);

}