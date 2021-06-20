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

import com.example.demo.model.ApplicationPhoto;
import com.example.demo.model.ApplicationVO;
import com.example.demo.model.ApplicationVO.ApplicationVOBuilder;
import com.example.demo.model.PhotoInfo;
import com.example.demo.model.Result;
import com.example.demo.service.ApplicationService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/application")
@RestController
public class ApplicationController {
	@Autowired
	ApplicationService service;
	
	@ApiImplicitParam(name="id", value="신청서 아이디", required=true, dataType="int")
	@GetMapping("/find/{id}")
	public Result findById(@PathVariable(name="id", required=true)Optional<Integer>applicationId) {
		Result result = Result.successInstance();
		ApplicationVOBuilder builder = ApplicationVO.builder();
		if(applicationId.isPresent()) {
			builder.id(applicationId.get());
		}
		ApplicationVO selected = service.selectOne(builder.build());
		
		List<PhotoInfo> photos = service.selectPhotos(selected);
		selected.setPhotos(photos);
		
		result.setData(selected);
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
		@ApiImplicitParam(name="writer_id", value="작성자 정보", required=true, dataType="int"),
		@ApiImplicitParam(name="capacity", value="발전소 용량", required=true, dataType="String"),
		@ApiImplicitParam(name="model", value="모델명", required=true, dataType="String"),
		@ApiImplicitParam(name="address", value="주소", required=true, dataType="String"),
		@ApiImplicitParam(name="pictures", allowMultiple=true, value="현장사진", required=true, dataType="String", paramType="body")
	})
	@ApiOperation(value="현장진단 신청서 작성", notes="현장진단 신청", response = Result.class)
	@PostMapping("/add")
	public Result insert(
			@RequestHeader String Authorization,
			@RequestParam int writer_id,
			@RequestParam String capacity,
			@RequestParam String model,
			@RequestParam String address,
			@RequestBody String[] pictures) {
		Result result = Result.successInstance();
		ApplicationVOBuilder builder = ApplicationVO.builder();
		builder.writer_id(writer_id);
		builder.capacity(capacity);
		builder.model(model);
		builder.address(address);
		ApplicationVO vo = builder.build();
		int insert = service.insert(vo);
		if(insert > 0) {
			result.setData(vo);
			result.setTotalCount(insert);
		}
		List<ApplicationPhoto> photos = new ArrayList<ApplicationPhoto>();
		for (String picture : pictures) {
			photos.add(new ApplicationPhoto(vo.getId(), Integer.parseInt(picture)));
		}
		service.insertPhotos(photos);
		
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
		@ApiImplicitParam(name="id", value="신청서 ID", required=true, dataType="int"),
		@ApiImplicitParam(name="capacity", value="발전소 용량", required=false, dataType="String"),
		@ApiImplicitParam(name="model", value="모델명", required=false, dataType="String"),
		@ApiImplicitParam(name="address", value="주소", required=false, dataType="String"),
		@ApiImplicitParam(name="pictures", allowMultiple=true, value="현장사진", required=true, dataType="String", paramType="body")
	})
	@ApiOperation(value="현장진단 신청서 수정", notes="현장진단 신청서 수정", response = Result.class)
	@PostMapping(value="/update")
	public Result update(@RequestHeader String Authorization,
			@RequestParam int id,
			@RequestParam String capacity,
			@RequestParam String model,
			@RequestParam String address,
			@RequestBody String[] pictures) {
		Result result = Result.successInstance();
		ApplicationVO vo = ApplicationVO.builder()
				.id(id)
				.capacity(capacity)
				.model(model)
				.address(address).build();
		int updateResult = service.update(vo);
		
		updatePhotoList(vo, pictures);
		result.setData(vo);
		result.setTotalCount(updateResult);
		return result;
	}
	
	@ApiOperation(value="신청서 삭제", notes="신청서 삭제", response= Result.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
		@ApiImplicitParam(name="id", value="신청서 아이디", required=true, dataType="int"),
	})	
	@PostMapping("/delete/{id}")
	public Result deleteById(@RequestHeader String Authorization,
			@PathVariable(name="id", required=true)Integer applicationId) {
		Result result = Result.successInstance();
		if(!(applicationId > 0)) {
			result = Result.failInstance();
			result.setMessage("id 파라미터 값이 유효하지 않습니다. 다시 확인 해주세요.");
		}else {
			ApplicationVOBuilder builder = ApplicationVO.builder();
			builder.id(applicationId);
			
			ApplicationVO vo = builder.build();
			int deleteResult = service.delete(vo);
			
			if(deleteResult > 0) {
				result.setMessage("삭제가 완료되었습니다.");
				List<PhotoInfo> photos = service.selectPhotos(vo);
				List<ApplicationPhoto> deletedPhotos = new ArrayList<ApplicationPhoto>();
				for(PhotoInfo photo: photos) {
					deletedPhotos.add(new ApplicationPhoto(vo.getId(), photo.getId()));
				}
				service.deletePhotos(deletedPhotos);
			}else {
				result = Result.failInstance();
				result.setMessage("이미 삭제가 된 게시글입니다.");
			}
		}
		return result;
	}
	
	private void updatePhotoList(ApplicationVO vo, String[] pictures) {
		List<Integer> pList = new ArrayList<Integer>();
		for(String picture : pictures) {
			pList.add(Integer.parseInt(picture));
		}
		pList.sort(null);
		
		List<ApplicationPhoto> newerPhotos = new ArrayList<ApplicationPhoto>();
		List<ApplicationPhoto> olderPhotos = new ArrayList<ApplicationPhoto>();
		
		List<PhotoInfo> photos = service.selectPhotos(vo);
		if(pList.size() == 0) {	
			// 새로 들어온 파일이 하나도 없을 경우
			for(PhotoInfo p:photos) {
				olderPhotos.add(new ApplicationPhoto(vo.getId(), p.getId()));
			}
		}else if(photos.size()==0 && pList.size()>0) {
			// 원래 있던 사진이 하나도 없고, 새로운 파일이 등록된경우
			Iterator<Integer> iter = pList.iterator();
			while(iter.hasNext()) {
				newerPhotos.add(new ApplicationPhoto(vo.getId(), iter.next()));
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
					newerPhotos.add(new ApplicationPhoto(vo.getId(), newPhotoId));
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
					olderPhotos.add(new ApplicationPhoto(vo.getId(), dbPhoto));
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
