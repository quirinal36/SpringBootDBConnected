package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.ProductPhoto;
import com.example.demo.model.ProductVO;


@Service
public class ProductService {
	@Autowired
	ProductMapper mapper;
	
	public int insert(ProductVO input) {
		return mapper.insert(input);
	}

	public int update(ProductVO input) {
		return mapper.update(input);
	}

	public List<ProductVO> selectAll() {
		return mapper.selectAll();
	}

	public List<ProductVO> select(ProductVO input) {
		input.setTotalCount(totalCount().size());
		return mapper.select(input);
	}

	public ProductVO selectOne(ProductVO input) {
		return mapper.selectOne(input);
	}

	public int delete(ProductVO input) {
		return mapper.delete(input);
	}

	public List<ProductVO> totalCount() {
		return mapper.totalCount();
	}
	public int insertPhotos(List<ProductPhoto> list) {
		if(list.size() == 0)return -1;
		return mapper.insertPhotos(list);
	}
}
