package com.example.documentdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentMetaData {

	private Long id; 
	private String name;
	private String type;
	private long size;
}
