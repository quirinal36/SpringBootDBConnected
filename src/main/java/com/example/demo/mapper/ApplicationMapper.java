package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.ApplicationPhoto;
import com.example.demo.model.ApplicationVO;
import com.example.demo.model.PhotoInfo;

@Mapper
public interface ApplicationMapper {
	public ApplicationVO selectById(ApplicationVO input);
	public int insert(ApplicationVO input);
	public int update(ApplicationVO input);
	public List<ApplicationVO> selectAll();
	public int delete(ApplicationVO input); 
	public int deletePhotos(List<ApplicationPhoto> input);
	public int insertPhotos(List<ApplicationPhoto> list);
	public List<PhotoInfo> selectPhotos(ApplicationVO input);
	public List<ApplicationVO> selectList();
}
