package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.WorkLinks;

@Mapper
public interface WorkLinksMapper {
	public List<WorkLinks> selectAll();
}
