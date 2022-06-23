package com.example.documentdemo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Table(name = "DOCUMENT_INFO", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "user_name" }))
@Entity
@Setter
@Getter
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "mime_type")
  private String mimeType;

  @Column(name = "size")
  private long size = 0;

  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "created_time", nullable = false)
  private Date createdTime;

  @Column(name = "updated_time")
  private Date updatedTime;
}