package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserVO;

@Service
public class UserService implements WorkService<UserVO> {
	@Autowired
	UserMapper mapper;
	
	@Value("${jwt.access.token.secure.key}")
	private String SECRET_KEY;
	
	@Override
	public List<UserVO> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public UserVO selectOne(UserVO input) {
		return mapper.selectUserByLogin(input);
	}
	
	public UserVO selectById(UserVO input) {
		return mapper.selectUserById(input);
	}
	public UserVO selectUserByLogin(UserVO input) {
		input.setEnc_key(SECRET_KEY);
		UserVO result = mapper.selectUserByLogin(input);
		if(result != null) {
			result.setPassword(null);
			result.setEnc_key(null);
		}
		return result;
	}
	public UserVO selectUserByEmail(String email) {
		UserVO input = new UserVO();
		input.setEmail(email);
		return mapper.selectUserByEmail(input);
	}
	@Override
	public int insert(UserVO input) {
		input.setEnc_key(SECRET_KEY);
		int result = mapper.insert(input);
		input.setEnc_key(null);
		return result;
	}

	@Override
	public int update(UserVO input) {
		return mapper.update(input);
	}
	
	
}
