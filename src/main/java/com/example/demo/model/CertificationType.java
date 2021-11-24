package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum CertificationType {
	PRODUCT(1, "제품"),
	COMPANY(2, "사업자");

	private int type;
	private String name;

	CertificationType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public static CertificationType convertToType(String name) {
		return valueOf(name);
	}
	public static CertificationType valueOf(int type) {
		for(CertificationType p : values()) {
			if(p.type == type) {
				return p;
			}
		}
		return null;
	}
	public static List<CertificationType> getAll() {
		List<CertificationType> result = new ArrayList<>();
		for(CertificationType type : values()) {
			result.add(type);
		}
		return result;
	}
}
