package com.example.demo.model;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class ConsultVO extends Paging{
	int id;
	String type;
	String body;
	int writer;
	String writerName;
	String phone;
	int status;
	Date wdate;
	String answerName;
	String answerBody;
	Date adate;
	
	@Builder
	public ConsultVO(int id, String type, String body, int writer, 
			String phone, int status, Date wdate, String answerName, String answerBody,
			Date adate, String writerName) {
		this.id=id;
		this.type=type;
		this.body=body;
		this.writer=writer;
		this.writerName=writerName;
		this.phone=phone;
		this.status=status;
		this.wdate=wdate;
		this.answerName=answerName;
		this.answerBody=answerBody;
		this.adate=adate;
	}
}
