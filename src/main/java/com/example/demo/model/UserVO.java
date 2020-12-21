package com.example.demo.model;

import java.util.Date;

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
	int user_role;
	int writer;
	String login;
	String password;
	String password_confirm;
	String username;
	String phone;
	String email;
	String domain;
	String classification;
	String level;
	String address;
	String addressDetail;
	String role_name;
	String role_name_kr;
	String telephone;
	Date mdate;
	Date udate;
	String sortBy;
	
	public static final int ROLE_ADMIN = 1;
	public static final int ROLE_STUDENT = 2;
	public static final int ROLE_GENERAL = 3;
	public static final int ROLE_COMPANY = 4;
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String STUDENT = "ROLE_STUDENT";
	public static final String GENERAL = "ROLE_GENERAL";
	public static final String COMPANY = "ROLE_COMPANY";
	
	public static final String[] ROLES = {"", ADMIN, STUDENT, GENERAL, COMPANY};
	
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
