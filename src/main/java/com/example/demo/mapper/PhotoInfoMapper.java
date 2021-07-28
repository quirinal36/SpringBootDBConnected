package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.PhotoInfo;


@Mapper
public interface PhotoInfoMapper {
	public int insertList(List<PhotoInfo> list);
	public int insert(PhotoInfo input);
	public PhotoInfo selectOne(PhotoInfo input);
	public int update(PhotoInfo input);
	public int delete(PhotoInfo input);
	public List<PhotoInfo> selectById(List<PhotoInfo> list);
	public List<PhotoInfo> select(PhotoInfo list);
	public int countAll();
}
