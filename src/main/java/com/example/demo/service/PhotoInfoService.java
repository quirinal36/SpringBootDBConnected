package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.PhotoInfoMapper;
import com.example.demo.model.PhotoInfo;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhotoInfoService implements WorkService<PhotoInfo> {
	@Autowired
	PhotoInfoMapper mapper;
	
	@Override
	public List<PhotoInfo> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PhotoInfo selectOne(PhotoInfo input) {
		return mapper.selectOne(input);
	}

	@Override
	public int insert(PhotoInfo input) {
		log.info(input.toString());
		return mapper.insert(input);
	}

	@Override
	public int update(PhotoInfo input) {
		return mapper.update(input);
	}
	public int insertList(List<PhotoInfo> list) {
		return mapper.insertList(list);
	}
	public int delete(PhotoInfo input) {
		return mapper.delete(input);
	}
	public List<PhotoInfo> selectById(List<PhotoInfo> list){
		return mapper.selectById(list);
	}
}
