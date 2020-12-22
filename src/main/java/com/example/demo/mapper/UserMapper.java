package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.UserVO;

@Mapper
public interface UserMapper {
	public UserVO selectUserByLogin(UserVO input);
	public int insert(UserVO input);
}
