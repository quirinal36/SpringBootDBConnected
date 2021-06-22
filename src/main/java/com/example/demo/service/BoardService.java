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
		return mapper.selectAll();
	}
	
	public List<BoardVO> select(BoardVO input) {
		int total = mapper.selectAll().size();
		input.setTotalCount(total);
		return mapper.select(input);
	}
	@Override
	public BoardVO selectOne(BoardVO input) {
		// TODO Auto-generated method stub
		return mapper.selectOne(input);
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
	
	public int delete(BoardVO input) {
		return mapper.delete(input);
	}
}
