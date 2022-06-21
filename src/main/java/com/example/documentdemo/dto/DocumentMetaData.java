package com.example.documentdemo.dto;

public class DocumentMetaData {

	private Long id; 
	private String name;
	private String type;
	private long size;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public long getSize() {
		return size;
	}

}
