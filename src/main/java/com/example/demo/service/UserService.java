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
		// TODO Auto-generated method stub
		return null;
	}
	
	public UserVO selectUserByLogin(UserVO input) {
		input.setEnc_key(SECRET_KEY);
		return mapper.selectUserByLogin(input);
	}

	@Override
	public int insert(UserVO input) {
		input.setEnc_key(SECRET_KEY);
		return mapper.insert(input);
	}

	@Override
	public int update(UserVO input) {
		return mapper.update(input);
	}
	
	
}
