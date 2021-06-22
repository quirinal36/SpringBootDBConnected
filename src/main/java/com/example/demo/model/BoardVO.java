package com.example.demo.model;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BoardVO extends Paging{
	int id;
	int writer;
	String title;
	String content;
	Date wdate;
	Date udate;
	String writerName;
	String writerEmail;
	
	@Builder
	public BoardVO(int id, String title, String content, String writerName, String writerEmail) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.writerName = writerName;
		this.writerEmail = writerEmail;
	}	
}
