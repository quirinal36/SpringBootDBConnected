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
public class ProductVO extends Paging{
	int id;
	String title;			// 제품명
	int capacity;			// 용량
	String model;			// 모델명
	String description;		// 설명
	String prdtCompany;		// 제조사
	String prdtDate;		// 제조연월일
	String usingPeriod;		// 사용기간
	int count;				// 수량
	int certificationId;	// 인증서 아이디
	int writer;
	String writerName;
	List<ProductPhoto> photos;
	Date wdate;
	Date udate;
	List<PhotoInfo> photoList;
	
	@Builder
	public ProductVO(int id, String title, int capacity,
			String model, String description, String prdtCompany,
			String prdtDate, String usingPeriod, int count,
			int certificationId, int writer, String writerName) {
		this.id = id;
		this.title = title;
		this.capacity = capacity;
		this.model = model;
		this.description = description;
		this.prdtCompany = prdtCompany;
		this.prdtDate = prdtDate;
		this.usingPeriod = usingPeriod;
		this.count = count;
		this.certificationId = certificationId;
		this.writer = writer;
		this.writerName = writerName;
	}
}
