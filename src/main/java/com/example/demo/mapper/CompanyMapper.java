package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.CompanyVO;

@Mapper
public interface CompanyMapper {
	public CompanyVO select(CompanyVO input);
	public List<CompanyVO> selectAll();
	public List<CompanyVO> selectByName(CompanyVO input);
	public CompanyVO selectByNameExactly(CompanyVO input);
	public int insert(CompanyVO input);
	public int update(CompanyVO input);
	public int delete(CompanyVO input);
}
