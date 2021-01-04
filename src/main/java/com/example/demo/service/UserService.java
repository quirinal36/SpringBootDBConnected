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
	
	@Value("${rest.api.key}")
	private String SECRET_KEY;
	
	@Override
	public List<UserVO> selectAll() {
		// TODO Auto-generated method stub
		return null;
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
		return mapper.insert(input);
	}
	
	
}
