package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.WorkLinksMapper;
import com.example.demo.model.WorkLinks;

@Service
public class WorkLinksService implements WorkService<WorkLinks> {
	@Autowired
	WorkLinksMapper mapper;
	
	@Override
	public List<WorkLinks> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public WorkLinks selectOne(WorkLinks input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(WorkLinks input) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
