package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.CertificationVO;

@Mapper
public interface CertificationMapper {
	public int insert(CertificationVO input);
	public int update(CertificationVO input);
	public CertificationVO selectById(CertificationVO input);
	public CertificationVO selectOne(CertificationVO input);
	public int delete(CertificationVO input);
}
