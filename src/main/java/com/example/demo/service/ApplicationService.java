package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.model.ApplicationPhoto;
import com.example.demo.model.ApplicationVO;
import com.example.demo.model.PhotoInfo;

@Service
public class ApplicationService implements WorkService<ApplicationVO> {
	@Autowired
	private ApplicationMapper mapper;

	@Override
	public List<ApplicationVO> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public ApplicationVO selectOne(ApplicationVO input) {
		return mapper.selectById(input);
	}

	@Override
	public int insert(ApplicationVO input) {
		return mapper.insert(input);
	}

	@Override
	public int update(ApplicationVO input) {
		return mapper.update(input);
	}
	
	public int delete(ApplicationVO input) {
		return mapper.delete(input);
	}
	public int deletePhotos(List<ApplicationPhoto> list) {
		if(list.size() == 0)return -1;
		return mapper.deletePhotos(list);
	}
	public int insertPhotos(List<ApplicationPhoto> list) {
		if(list.size() == 0)return -1;
		return mapper.insertPhotos(list);
	}
	
	public List<PhotoInfo> selectPhotos(ApplicationVO input){
		return mapper.selectPhotos(input);
	}
}
