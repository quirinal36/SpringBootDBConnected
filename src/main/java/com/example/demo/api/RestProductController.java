package com.example.demo.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.PagingResult;
import com.example.demo.model.PhotoInfo;
import com.example.demo.model.ProductPhoto;
import com.example.demo.model.ProductVO;
import com.example.demo.model.Result;
import com.example.demo.service.ProductService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/product/")
@RestController
public class RestProductController {
	@Autowired
	ProductService service;
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="title", value="제목", required=true, dataType="String"),
		@ApiImplicitParam(name="capacity", value="용량", required=false, dataType="int"),
		@ApiImplicitParam(name="model", value="모델명", required=true, dataType="String"),
		@ApiImplicitParam(name="description", value="상세설명", required=false, dataType="String"),
		@ApiImplicitParam(name="prdtCompany", value="회사명", required=false, dataType="String"),
		@ApiImplicitParam(name="prdtDate", value="제조연월", required=false, dataType="String"),
		@ApiImplicitParam(name="usingPeriod", value="사용기간", required=false, dataType="String"),
		@ApiImplicitParam(name="count", value="갯수", required=false, dataType="int"),
		@ApiImplicitParam(name="writer", value="작성자 아이디", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
		@ApiImplicitParam(name="pictures", allowMultiple=true, value="현장사진", required=true, dataType="String", paramType="body"),
	})
	@ApiOperation(value="상품등록", notes="상품등록", response = Result.class)
	@PostMapping(value="add")
	public Result add(
			@RequestHeader String Authorization,
			@RequestParam(value="title", required=true) String title,
			@RequestParam(value="capacity", required=false) Optional<Integer>capacity,
			@RequestParam(value="model", required=true) String model,
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="prdtCompany", required=false) String prdtCompany,
			@RequestParam(value="prdtDate", required=false) String prdtDate,
			@RequestParam(value="usingPeriod", required=false) String usingPeriod,
			@RequestParam(value="count", required=false) Optional<Integer> count,
			@RequestParam int writer,
			@RequestBody String[] pictures) {
		log.info("add");
		
		Result result = Result.successInstance();
		ProductVO vo = new ProductVO();
		vo.setTitle(title);
		if(count.isPresent()) {
			vo.setCount(count.get());
		}else {
			vo.setCount(0);
		}
		if(capacity.isPresent()) {
			vo.setCapacity(capacity.get());
		}else {
			vo.setCapacity(0);
		}
		vo.setWriter(writer);
		vo.setModel(model);
		
		service.insert(vo);
		
		List<ProductPhoto> photos = new ArrayList<ProductPhoto>();
		for (String picture : pictures) {
			photos.add(new ProductPhoto(vo.getId(), Integer.parseInt(picture)));
		}
		service.insertPhotos(photos);
		vo.setPhotos(photos);
		result.setData(vo);
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="title", value="제목", required=false, dataType="String"),
		@ApiImplicitParam(name="capacity", value="용량", required=false, dataType="int"),
		@ApiImplicitParam(name="model", value="모델명", required=false, dataType="String"),
		@ApiImplicitParam(name="description", value="상세설명", required=false, dataType="String"),
		@ApiImplicitParam(name="prdtCompany", value="회사명", required=false, dataType="String"),
		@ApiImplicitParam(name="prdtDate", value="제조연월", required=false, dataType="String"),
		@ApiImplicitParam(name="usingPeriod", value="사용기간", required=false, dataType="String"),
		@ApiImplicitParam(name="count", value="갯수", required=false, dataType="int"),
		@ApiImplicitParam(name="writer", value="작성자 아이디", required=true, dataType="int"),
		@ApiImplicitParam(name="certificationId", value="인증서 아이디", required=false, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@ApiOperation(value="상품수정", notes="상품수정", response = Result.class)
	@PostMapping(value="update")
	public Result update(
			@RequestParam(value="id")int id,
			@RequestHeader String Authorization,
			@RequestParam(value="certificationId", required=false) Optional<Integer>certificationId,
			@RequestParam(value="title", required=false) Optional<String> title,
			@RequestParam(value="capacity", required=false) Optional<Integer> capacity,
			@RequestParam(value="model", required=false) Optional<String> model,
			@RequestParam(value="description", required=false) Optional<String> description,
			@RequestParam(value="prdtCompany", required=false) Optional<String> prdtCompany,
			@RequestParam(value="prdtDate", required=false) Optional<String> prdtDate,
			@RequestParam(value="usingPeriod", required=false) Optional<String> usingPeriod,
			@RequestParam(value="count", required=false) Optional<Integer> count,
			@RequestParam(value="writer")Optional<Integer>writer,
			@RequestBody String[] pictures) {
		Result result = Result.successInstance();
		ProductVO vo = new ProductVO();
		vo.setId(id);
		ProductVO selected = service.selectOne(vo);
		if(certificationId.isPresent()) {
			selected.setCertificationId(certificationId.get());
		}
		if(title.isPresent()) {
			selected.setTitle(title.get());
		}
		if(capacity.isPresent()) {
			selected.setCapacity(capacity.get());
		}else {
			selected.setCapacity(0);
		}
		if(model.isPresent()) {
			selected.setModel(model.get());
		}
		if(description.isPresent()) {
			selected.setDescription(description.get());
		}
		if(prdtCompany.isPresent()) {
			selected.setPrdtCompany(prdtCompany.get());
		}
		if(prdtDate.isPresent()) {
			selected.setPrdtDate(prdtDate.get());
		}
		if(usingPeriod.isPresent()) {
			selected.setUsingPeriod(usingPeriod.get());
		}
		if(count.isPresent()) {
			selected.setCount(count.get());
		}else {
			selected.setCount(0);
		}
		if(writer.isPresent()) {
			selected.setWriter(writer.get());
		}
		if(service.update(selected) > 0) {
			result.setData(selected);
			result.setTotalCount(1);
			updatePhotoList(vo, pictures);
		}
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="query", value="검색어", required=false, dataType = "String"),
		@ApiImplicitParam(name="pageNo", value="페이지번호", required=false, dataType = "int"),
	})
	@ApiOperation(value="상품 리스트", notes="1 페이지당 10개 상품을 리스트한다.", response=Result.class)
	@GetMapping(value="list/{pageNo}")
	public PagingResult select(@RequestParam(value="query", required=false)Optional<String> query,
			@PathVariable(value="pageNo", required=false)Optional<Integer> pageNo,
			@RequestParam(value="size", required=false)Optional<Integer>size){
		
		ProductVO vo = new ProductVO();
		if(pageNo.isPresent()) {
			vo.setPageNo(pageNo.get());
		}else {
			vo.setPageNo(1);
		}
		if(query.isPresent()) {
			vo.setQuery(query.get());
		}
		if(!size.isPresent()) {
			vo.setPageSize(10);
		}else {
			vo.setPageSize(size.get());
		}
		
		PagingResult result = PagingResult.successInstance();
		List<ProductVO> list = service.select(vo);
		result.setPageNo(vo.getPageNo());
		result.setTotalCount(service.totalCount().size());
		Iterator<ProductVO> iter = list.iterator();
		while(iter.hasNext()) {
			ProductVO product = iter.next();
			List<PhotoInfo> photos = service.selectPhotos(product);
			product.setPhotoList(photos);
		}
		result.setData(list);
		return result;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="status", value="0-대기, 1-진행중, 2-완료", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header")
	})
	@ApiOperation(value="거래 상태 변경", notes="거래 상태 변경", response = Result.class)
	@PostMapping(value="update/status")
	public Result changeStatus(
			@RequestParam(value="id")int id,
			@RequestHeader String Authorization,
			@RequestParam(value="status")int status
			) {
		Result result = Result.successInstance();
		
		ProductVO vo = new ProductVO();
		vo.setId(id);
		ProductVO selected = service.selectOne(vo);
		selected.setStatus(status);
		if(service.changeStatus(selected)>0) {
			result.setData(selected);
			result.setTotalCount(1);
		}
		
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="buyer", value="buyer", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header")
	})
	@ApiOperation(value="구매 요청", notes="구매 요청", response=Result.class)
	@PostMapping(value="request")
	public Result requestPurchase(
			@RequestParam(value="id", required=true)int id,
			@RequestHeader String Authorization,
			@RequestParam(value="buyer", required=true)int buyer) {
		Result result = Result.successInstance();
		
		ProductVO vo = new ProductVO();
		vo.setId(id);
		ProductVO selected = service.selectOne(vo);
		selected.setBuyer(buyer);
		if(service.requestPurchase(selected)>0) {
			result.setData(selected);
			result.setTotalCount(1);
		}
		
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header")
	})
	@ApiOperation(value="구매 요청 취소", notes="구매 요청 취소", response=Result.class)
	@PostMapping(value="request/cancel")
	public Result cancelRequestPurchase(
			@RequestParam(value="id", required=true)int id,
			@RequestHeader String Authorization) {
		Result result = Result.successInstance();
		
		ProductVO vo = new ProductVO();
		vo.setId(id);
		ProductVO selected = service.selectOne(vo);
		selected.setStatus(0);
		selected.setBuyer(0);
		if(service.changeStatus(selected)>0) {
			result.setData(selected);
			result.setTotalCount(1);
		}
		
		return result;
	}

	@ApiImplicitParam(name="id", value="id", required=true, dataType="int")
	@ApiOperation(value="제품 인증서 삭제", notes="제품 인증서 삭제", response=Result.class)
	@GetMapping(value="/certification/delete/{id}")
	public Result deleteCertification(@PathVariable(value="id", required=true)int id) {
		Result result = Result.successInstance();
		
		ProductVO vo = new ProductVO();
		vo.setId(id);
		ProductVO selected = service.selectOne(vo);
		if(service.deleteCertification(selected)>0){
			result.setData(selected);
			result.setTotalCount(1);
		}
		
		return result;
	}
	
	
	@ApiImplicitParam(name="id", value="id", required=true, dataType="int")
	@ApiOperation(value="상품 상세화면", notes="상품 상세화면", response=Result.class)
	@GetMapping(value="/detail/{id}")
	public Result selectOne(@PathVariable(value="id", required=true)int id) {
		Result result = Result.successInstance();
		ProductVO vo = new ProductVO();
		vo.setId(id);
		ProductVO selected = service.selectOne(vo);
		List<PhotoInfo> photos = service.selectPhotos(vo);
		selected.setPhotoList(photos);
		result.setData(selected);
		if(selected.getId() > 0) {
			result.setTotalCount(1);
		}
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@ApiOperation(value="상품 삭제하기", notes="상품 삭제하기", response=Result.class)
	@PostMapping(value="/delete/{id}")
	public Result delete(@PathVariable(value="id", required=true)int id,
			@RequestHeader String Authorization) {
		Result result = Result.successInstance();
		ProductVO vo = new ProductVO();
		vo.setId(id);
		List<PhotoInfo> selctedPhotos = service.selectPhotos(vo);
		List<ProductPhoto> list = new ArrayList<ProductPhoto>();
		for(PhotoInfo info: selctedPhotos) {
			list.add(new ProductPhoto(vo.getId(), info.getId()));
		}
		service.deletePhotos(list);
		int deleted = service.delete(vo);
		result.setData(deleted);
		
		return result;
	}
	private void updatePhotoList(ProductVO vo, String[] pictures) {
		List<Integer> pList = new ArrayList<Integer>();
		for(String picture : pictures) {
			pList.add(Integer.parseInt(picture));
		}
		pList.sort(null);
		
		List<ProductPhoto> newerPhotos = new ArrayList<ProductPhoto>();
		List<ProductPhoto> olderPhotos = new ArrayList<ProductPhoto>();
		
		List<PhotoInfo> photos = service.selectPhotos(vo);
		if(pList.size() == 0) {	
			// 새로 들어온 파일이 하나도 없을 경우
			for(PhotoInfo p:photos) {
				olderPhotos.add(new ProductPhoto(vo.getId(), p.getId()));
			}
		}else if(photos.size()==0 && pList.size()>0) {
			// 원래 있던 사진이 하나도 없고, 새로운 파일이 등록된경우
			Iterator<Integer> iter = pList.iterator();
			while(iter.hasNext()) {
				newerPhotos.add(new ProductPhoto(vo.getId(), iter.next()));
			}
		}else {
			Iterator<Integer> iter = pList.iterator();
			
			while(iter.hasNext()) {
				int newPhotoId = iter.next();
				boolean isExist = false;
				for(PhotoInfo p: photos) {
					if(p.getId() == newPhotoId) {
						isExist = true;
						break;
					}
				}
				if(!isExist) {
					newerPhotos.add(new ProductPhoto(vo.getId(), newPhotoId));
				}
			}
			
			for(PhotoInfo p: photos) {
				int dbPhoto = p.getId();
				boolean isDeleted = true;
				for(Integer newPhotoId : pList) {
					if(newPhotoId == dbPhoto) {
						isDeleted = false;
						break;
					}
				}
				if(isDeleted) {
					olderPhotos.add(new ProductPhoto(vo.getId(), dbPhoto));
				}
			}
		}
		if(newerPhotos.size()>0) {
			service.insertPhotos(newerPhotos);
		}
		if(olderPhotos.size() > 0) {
			service.deletePhotos(olderPhotos);
		}
	}
}
