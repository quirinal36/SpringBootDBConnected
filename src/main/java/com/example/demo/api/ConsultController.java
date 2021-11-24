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
import com.example.demo.model.ConsultVO;
import com.example.demo.model.Result;
import com.example.demo.service.ConsultService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/consult/")
@RestController
public class ConsultController {
	@Autowired
	ConsultService service;
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="type", value="분류", required=true, dataType="String"),
		@ApiImplicitParam(name="body", value="상담 내용", required=true, dataType="String"),
		@ApiImplicitParam(name="writer", value="작성자", required=true, dataType="String"),
		@ApiImplicitParam(name="writerName", value="작성자 이름", required=true, dataType="String"),
		@ApiImplicitParam(name="phone", value="연락처", required=true, dataType="String"),
		@ApiImplicitParam(name="status", value="답변 상태", required=false, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@ApiOperation(value="상담 등록", notes="상담 등록", response = Result.class)
	@PostMapping(value="add")
	public Result add(
			@RequestHeader String Authorization,
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="body", required=true) String body,
			@RequestParam(value="writerName", required=true) String writerName,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="status", required=false) Optional<Integer> status,
			@RequestParam int writer) {
		log.info("add");
		
		Result result = Result.successInstance();
		ConsultVO vo = new ConsultVO();
		vo.setType(type);
		vo.setBody(body);
		vo.setWriter(writer);
		vo.setWriterName(writerName);
		vo.setPhone(phone);
		if(status.isPresent()) {
			vo.setStatus(status.get());
		}else {
			vo.setStatus(0);
		}
		
		service.insert(vo);
		
		result.setData(vo);
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="answerName", value="답변자 이름", required=true, dataType="String"),
		@ApiImplicitParam(name="answerBody", value="답변 내용", required=true, dataType="String"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@ApiOperation(value="답변 등록", notes="답변 등록", response = Result.class)
	@PostMapping(value="add/answer")
	public Result addAnswer(
			@RequestParam(value="id")int id,
			@RequestHeader String Authorization,
			@RequestParam(value="answerName", required=true) String answerName,
			@RequestParam(value="answerBody", required=true) String answerBody
			) {
		Result result = Result.successInstance();
		ConsultVO vo = new ConsultVO();
		vo.setId(id);
		
		vo.setAnswerName(answerName);
		vo.setAnswerBody(answerBody);
		vo.setStatus(1);
		
		if(service.addAnswer(vo) > 0) {
			result.setData(vo);
			result.setTotalCount(1);
		}
		return result;
	}
	
	@ApiOperation(value="답변 삭제", notes="답변 삭제", response = Result.class)
	@PostMapping(value="delete/answer")
	public Result deleteAnswer(
			@RequestParam(value="id")int id,
			@RequestHeader String Authorization
			) {
		Result result = Result.successInstance();
		ConsultVO vo = new ConsultVO();
		vo.setId(id);
		
		if(service.addAnswer(vo) > 0) {
			result.setData(vo);
			result.setTotalCount(1);
		}
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="query", value="검색어", required=false, dataType = "String"),
		@ApiImplicitParam(name="pageNo", value="페이지번호", required=false, dataType = "int"),
	})
	@ApiOperation(value="상담 리스트", notes="상담 리스트를 불러온다.", response=Result.class)
	@GetMapping(value="list/{pageNo}")
	public PagingResult selectList(@RequestParam(value="query", required=false)Optional<String> query,
			@PathVariable(value="pageNo", required=false)Optional<Integer> pageNo,
			@RequestParam(value="size", required=false)Optional<Integer>size){
		
		ConsultVO vo = new ConsultVO();
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
		List<ConsultVO> list = service.selectAll(vo);
		result.setPageNo(vo.getPageNo());
		result.setTotalCount(service.totalCount().size());		
		
		result.setData(list);
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="query", value="검색어", required=false, dataType = "String"),
		@ApiImplicitParam(name="pageNo", value="페이지번호", required=false, dataType = "int"),
	})
	@ApiOperation(value="상담 리스트 - 답변 상태", notes="답변 상태 기준으로 리스트를 불러온다.", response=Result.class)
	@GetMapping(value="list/status/{pageNo}")
	public PagingResult selectByStatus(@RequestParam(value="query", required=false)Optional<String> query,
			@PathVariable(value="pageNo", required=false)Optional<Integer> pageNo,
			@RequestParam(value="size", required=false)Optional<Integer>size){
		
		ConsultVO vo = new ConsultVO();
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
		List<ConsultVO> list = service.selectByStatus(vo);
		result.setPageNo(vo.getPageNo());
		result.setTotalCount(service.totalCount().size());

		result.setData(list);
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="query", value="검색어", required=false, dataType = "String"),
		@ApiImplicitParam(name="pageNo", value="페이지번호", required=false, dataType = "int"),
	})
	@ApiOperation(value="상담 리스트 - 분류", notes="상담 분류 기준으로 리스트를 불러온다.", response=Result.class)
	@GetMapping(value="list/type/{pageNo}")
	public PagingResult selectByType(@RequestParam(value="query", required=false)Optional<String> query,
			@PathVariable(value="pageNo", required=false)Optional<Integer> pageNo,
			@RequestParam(value="size", required=false)Optional<Integer>size){
		
		ConsultVO vo = new ConsultVO();
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
		List<ConsultVO> list = service.selectByType(vo);
		result.setPageNo(vo.getPageNo());
		result.setTotalCount(service.totalCount().size());

		result.setData(list);
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="query", value="검색어", required=false, dataType = "String"),
		@ApiImplicitParam(name="pageNo", value="페이지번호", required=false, dataType = "int"),
	})
	@ApiOperation(value="상담 리스트 - 작성자", notes="신청자 이름 기준으로 리스트를 불러온다.", response=Result.class)
	@GetMapping(value="list/name/{pageNo}")
	public PagingResult selectByName(@RequestParam(value="query", required=false)Optional<String> query,
			@PathVariable(value="pageNo", required=false)Optional<Integer> pageNo,
			@RequestParam(value="size", required=false)Optional<Integer>size){
		
		ConsultVO vo = new ConsultVO();
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
		List<ConsultVO> list = service.selectByName(vo);
		result.setPageNo(vo.getPageNo());
		result.setTotalCount(service.totalCount().size());

		result.setData(list);
		return result;
	}
	
	@ApiImplicitParam(name="id", value="id", required=true, dataType="int")
	@ApiOperation(value="상담 상세 내용", notes="상담 상세 내용", response=Result.class)
	@GetMapping(value="detail/{id}")
	public Result selectOne(@PathVariable(value="id", required=true)int id) {
		Result result = Result.successInstance();
		ConsultVO vo = new ConsultVO();
		vo.setId(id);
		ConsultVO selected = service.selectOne(vo);
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
	@PostMapping(value="delete/{id}")
	public Result delete(@PathVariable(value="id", required=true)int id,
			@RequestHeader String Authorization) {
		Result result = Result.successInstance();
		ConsultVO vo = new ConsultVO();
		vo.setId(id);
		int deleted = service.delete(vo);
		result.setData(deleted);
		
		return result;
	}
}
