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
public class PhotoInfo {
	public static PhotoInfo newInstance(int boardId) {
		PhotoInfo result = new PhotoInfo();
		return result;
	}
	
	int id;
	int uploader;
	Date wdate;
	String url;
	String name;
	String newFilename;
	int size;
	String contentType;
	Date udate;
	int type;
	String thumbnailFilename;
	int thumbnailSize;
	String thumbnailUrl;
	
	public PhotoInfo(int id, int uploader, Date wdate, 
			String url, String name, String newFilename
			,int size, String contentType,Date udate, int type,
			String thumbnailFilename, int thumbnailSize, String thumbnailUrl) {
		this.id = id;
		this.uploader = uploader;
		this.wdate = wdate;
		this.udate = udate;
		this.type = type;
		this.size = size;
		this.url = url;
		this.name = name;
		this.newFilename = newFilename;
		this.contentType = contentType;
		this.thumbnailFilename = thumbnailFilename;
		this.thumbnailSize = thumbnailSize;
		this.thumbnailUrl = thumbnailUrl;
	}
}
