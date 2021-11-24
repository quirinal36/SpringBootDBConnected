package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.PhotoInfo;
import com.example.demo.model.ProductPhoto;
import com.example.demo.model.ProductVO;


@Mapper
public interface ProductMapper {
	public int insert(ProductVO input);
	public int update(ProductVO input);
	public List<ProductVO> selectAll();
	public List<ProductVO> select(ProductVO input);
	public ProductVO selectOne(ProductVO input);
	public int delete(ProductVO input);
	public List<ProductVO> totalCount();
	public int insertPhotos(List<ProductPhoto> list);
	public int deletePhotos(List<ProductPhoto>list);
	public List<PhotoInfo> selectPhotos(ProductVO input);
	public int changeStatus(ProductVO input);
	public int requestPurchase(ProductVO input);
	public int deleteCertification(ProductVO input);
}