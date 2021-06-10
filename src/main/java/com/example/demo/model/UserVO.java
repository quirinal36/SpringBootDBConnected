package com.example.demo.model;

import com.example.demo.conf.auth.dto.AuthProvider;
import com.example.demo.security.Role;

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
	String role_name;
	Role roleType;
	
	String login;
	String password;
	String name;
	String email;
	String picture;
	String phone;
	String fax;
	String mobile;
	
	int pictureId;
	int companyId;
	
	private String enc_key;
	
	private AuthProvider provider;
	private String providerId;
	
//	public static final String AUTH = "Authorization";
	
	public static UserVO newInstanse(int id) {
		UserVO user = new UserVO();
		user.setId(id);
		return user;
	}
	
	@Builder
	public UserVO(int id, String name, String login, String email, String picture, Role roleType, String password,
			String phone, String fax, String mobile, int pictureId, int companyId) {
		this.id = id;
		this.name = name;
		this.login = login;
		this.picture = picture;
		this.roleType = roleType;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.fax = fax;
		this.mobile = mobile;
		this.pictureId = pictureId;
		this.companyId = companyId;
		
		if(roleType!=null) {
			if(roleType.getCode().equalsIgnoreCase(Role.ADMIN.getCode())) {
				this.role = 1;
			}else if(roleType.getCode().equalsIgnoreCase(Role.SELLER.getCode())) {
				this.role = 2;
			}else if(roleType.getCode().equalsIgnoreCase(Role.BUYER.getCode())) {
				this.role = 3;
			}
		}
	}
	public String getRoleKey() {
		if(this.roleType != null) {
			return this.roleType.getCode();
		}else {
			return "";
		}
	}
}
