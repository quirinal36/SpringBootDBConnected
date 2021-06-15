package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum PhotoType {
	REGISTRATION(1, "사업자등록증"),
	USER_PROFILE(2, "사용자 프로필사진"), 
	PRODUCT(3, "제품사진"); 

	private int type;
	private String name;

	PhotoType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public static PhotoType convertToType(String name) {
		return valueOf(name);
	}
	public static PhotoType valueOf(int type) {
		for(PhotoType p : values()) {
			if(p.type == type) {
				return p;
			}
		}
		return null;
	}
	public static List<PhotoType> getAll() {
		List<PhotoType> result = new ArrayList<>();
		for(PhotoType type : values()) {
			result.add(type);
		}
		return result;
	}
}
