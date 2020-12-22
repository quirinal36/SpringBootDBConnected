package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.BookInfoTableMapper;
import com.example.demo.model.BookInfo;

@Service
public class BookService implements WorkService<BookInfo> {
	@Autowired
	BookInfoTableMapper mapper;
	
	@Override
	public List<BookInfo> selectAll() {
		return mapper.getBookInfoList();
	}

	@Override
	public BookInfo selectOne(BookInfo input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(BookInfo input) {
		// TODO Auto-generated method stub
		return 0;
	}

}
