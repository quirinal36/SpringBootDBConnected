package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CertificationMapper;
import com.example.demo.model.CertificationVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CertificationService implements WorkService<CertificationVO> {
	@Autowired
	private CertificationMapper mapper;
	@Override
	public List<CertificationVO> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CertificationVO selectOne(CertificationVO input) {
		return mapper.selectOne(input);
	}

	public CertificationVO selectById(CertificationVO input) {
		return mapper.selectById(input);
	}

	@Override
	public int insert(CertificationVO input) {
		return mapper.insert(input);
	}

	@Override
	public int update(CertificationVO input) {
		// TODO Auto-generated method stub
		return mapper.update(input);
	}
	
	public int delete(CertificationVO input) {
		return mapper.delete(input);
	}

}
