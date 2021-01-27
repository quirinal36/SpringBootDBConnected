package com.example.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
	ADMIN("ROLE_ADMIN", "관리자"),
	USER("ROLE_USER", "회원");
	
	private final String key;
	private final String title;
}
