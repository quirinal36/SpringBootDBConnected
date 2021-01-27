package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserVO extends BaseTimeEntity{
	int id;
	int role;
	String login;
	String password;
	String name;
	String role_name;
	String email;
	String picture;
	Role roleType;
	
	private String enc_key;
	
	public static final String AUTH = "Authorization";
	
	public static UserVO newInstanse(int id) {
		UserVO user = new UserVO();
		user.setId(id);
		return user;
	}
}
