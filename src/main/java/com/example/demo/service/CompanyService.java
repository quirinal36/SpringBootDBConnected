package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CompanyMapper;
import com.example.demo.model.CompanyVO;

@Service
public class CompanyService implements WorkService<CompanyVO> {
	@Autowired
	CompanyMapper mapper;

	@Override
	public List<CompanyVO> selectAll() {
		return mapper.selectAll();
	}
	public List<CompanyVO> selectByName(CompanyVO input) {
		return mapper.selectByName(input);
	}
	public CompanyVO selectByNameExactly(CompanyVO input) {
		return mapper.selectByNameExactly(input);
	}
	@Override
	public CompanyVO selectOne(CompanyVO input) {
		return mapper.select(input);
	}

	@Override
	public int insert(CompanyVO input) {
		return mapper.insert(input);
	}

	@Override
	public int update(CompanyVO input) {
		return mapper.update(input);
	}
	
	public int delete(CompanyVO input) {
		return mapper.delete(input);
	}
}
