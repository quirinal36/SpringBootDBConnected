package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ConsultMapper;
import com.example.demo.model.ConsultVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsultService {
	@Autowired
	ConsultMapper mapper;
	
	public int insert(ConsultVO input) {
		return mapper.insert(input);
	}

	public List<ConsultVO> selectAll(ConsultVO input) {
		return mapper.selectAll(input);
	}

	public List<ConsultVO> selectByName(ConsultVO input) {
		int total = totalCount().size();
		input.setTotalCount(total);
		
		return mapper.selectByName(input);
	}
	
	public List<ConsultVO> selectByType(ConsultVO input) {
		int total = totalCount().size();
		input.setTotalCount(total);
		
		return mapper.selectByType(input);
	}
	
	public List<ConsultVO> selectByStatus(ConsultVO input) {
		int total = totalCount().size();
		input.setTotalCount(total);
		
		return mapper.selectByStatus(input);
	}
	
	public ConsultVO selectOne(ConsultVO input) {
		return mapper.selectOne(input);
	}

	public int delete(ConsultVO input) {
		return mapper.delete(input);
	}

	public List<ConsultVO> totalCount() {
		return mapper.totalCount();
	}
	
	public int deleteAnswer(ConsultVO input) {
		return mapper.deleteAnswer(input);
	}
	
	public int addAnswer(ConsultVO input) {
		return mapper.addAnswer(input);
	}
}
