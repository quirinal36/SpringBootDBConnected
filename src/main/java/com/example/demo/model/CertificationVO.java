package com.example.demo.model;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class CertificationVO {
	int id;
	int writer_id;
	String url;
	String name;
	String newFilename;
	int size;
	String contentType;
	int type;
	int status;
	Date wdate;
	Date udate;
	
	public CertificationVO(int id, int writer_id,  
			String url, String name, String newFilename
			,int size, String contentType,int type,int status, Date wdate,Date udate) {
		this.id = id;
		this.writer_id = writer_id;
		this.wdate = wdate;
		this.udate = udate;
		this.size = size;
		this.url = url;
		this.name = name;
		this.status=status;
		this.newFilename = newFilename;
		this.contentType = contentType;
		this.type=type;
	}
}
