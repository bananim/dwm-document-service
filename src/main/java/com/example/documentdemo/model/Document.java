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

  /*
   * public String getUserName() { return userName; }
   * 
   * public void setUserName(String userName) { this.userName = userName; }
   * 
   * public Long getId() { return id; }
   * 
   * public String getName() { return name; }
   * 
   * public void setName(String name) { this.name = name; }
   * 
   * public String getMimeType() { return mimeType; }
   * 
   * public void setMimeType(String mimeType) { this.mimeType = mimeType; }
   * 
   * public long getSize() { return size; }
   * 
   * public void setSize(long size) { this.size = size; }
   * 
   * public Date getCreatedTime() { return createdTime; }
   * 
   * public void setCreatedTime(Date createdTime) { this.createdTime =
   * createdTime; }
   * 
   * public Date getUpdatedTime() { return updatedTime; }
   * 
   * public void setUpdatedTime(Date updatedTime) { this.updatedTime =
   * updatedTime; }
   */
}