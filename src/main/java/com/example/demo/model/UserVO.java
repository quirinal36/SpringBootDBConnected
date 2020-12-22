package com.example.demo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
	
	final String enc_key = "solasido";
	public static final String AUTH = "Authorization";
	
	public static final int ROLE_ADMIN = 1;
	public static final int ROLE_USER = 2;
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER = "ROLE_USER";
	
	public static final String[] ROLES = {"", ADMIN, USER};
	
	public UserVO () {
		
	}
	public static UserVO newInstanse(int id) {
		UserVO user = new UserVO();
		user.setId(id);
		return user;
	}
	public String toJsonString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
