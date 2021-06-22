package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.BoardVO;


@Mapper
public interface BoardMapper {
	public int insert(BoardVO input);
	public int update(BoardVO input);
	public List<BoardVO> selectAll();
	public List<BoardVO> select(BoardVO input);
	public BoardVO selectOne(BoardVO input);
	public int delete(BoardVO input);
	public List<BoardVO> totalCount();
}
