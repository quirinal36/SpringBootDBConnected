package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserVO {
	int id;
	int role;
	String login;
	String password;
	String username;
	String role_name;
	
	private String enc_key;
	
	public static final String AUTH = "Authorization";
	
	public static final int ROLE_ADMIN = 1;
	public static final int ROLE_USER = 2;
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER = "ROLE_USER";
	
	public static final String[] ROLES = {"", ADMIN, USER};
	
	public static UserVO newInstanse(int id) {
		UserVO user = new UserVO();
		user.setId(id);
		return user;
	}
}
