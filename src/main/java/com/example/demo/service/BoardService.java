package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.BoardMapper;
import com.example.demo.model.BoardVO;

@Service
public class BoardService implements WorkService<BoardVO> {
	@Autowired
	private BoardMapper mapper;
	
	@Override
	public List<BoardVO> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public BoardVO selectOne(BoardVO input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(BoardVO input) {
		// TODO Auto-generated method stub
		return mapper.insert(input);
	}

	@Override
	public int update(BoardVO input) {
		// TODO Auto-generated method stub
		return mapper.update(input);
	}

}
