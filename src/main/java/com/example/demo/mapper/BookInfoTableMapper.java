package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BookInfo;

@Mapper
public interface BookInfoTableMapper {
	public BookInfo getBookInfo(BookInfo input);
	public List<BookInfo> getBookInfoList();
	public int insert(BookInfo input);
	public int update(BookInfo input);
	public int delete(BookInfo input);
}
