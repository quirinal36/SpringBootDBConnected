package com.example.demo.model;

import lombok.Builder;
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
	
//	public static final String AUTH = "Authorization";
	
	public static UserVO newInstanse(int id) {
		UserVO user = new UserVO();
		user.setId(id);
		return user;
	}
	@Builder
	public UserVO(String name, String login, String email, String picture, Role roleType) {
		this.name = name;
		this.login = login;
		this.picture = picture;
		this.roleType = roleType;
		if(roleType.getKey().equals("ROLE_ADMIN")) {
			this.role = 1;
		}else if(roleType.getKey().equals("ROLE_USER")) {
			this.role = 2;
		}
	}
	public String getRoleKey() {
		return this.roleType.getKey();
	}
}
