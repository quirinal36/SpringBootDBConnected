package com.example.demo.model;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApplicationVO {
	int id;
	int writer_id;
	String capacity;
	String model;
	String address;
	Date wdate;
	Date udate;
	String username;
	List<PhotoInfo> photos;
	
	@Builder
	public ApplicationVO(int id, int writer_id,
			String capacity, 
			String model,
			String address) {
		this.id = id;
		this.writer_id = writer_id;
		this.capacity = capacity;
		this.model = model;
		this.address = address;
	}
}
