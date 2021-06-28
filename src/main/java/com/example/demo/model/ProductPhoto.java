package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ProductPhoto {
	int productId;
	int photoId;
	
	@Builder
	public ProductPhoto(int aid, int pid) {
		this.productId = aid;
		this.photoId = pid;
	}
}
