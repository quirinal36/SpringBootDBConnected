package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.ConsultVO;


@Mapper
public interface ConsultMapper {
	public int insert(ConsultVO input);
	public List<ConsultVO> selectAll(ConsultVO input);
	public List<ConsultVO> selectByType(ConsultVO input);
	public List<ConsultVO> selectByName(ConsultVO input);
	public List<ConsultVO> selectByStatus(ConsultVO input);
	public ConsultVO selectOne(ConsultVO input);
	public int delete(ConsultVO input);
	public int addAnswer(ConsultVO input);
	public int deleteAnswer(ConsultVO input);
	public List<ConsultVO> totalCount();
}
