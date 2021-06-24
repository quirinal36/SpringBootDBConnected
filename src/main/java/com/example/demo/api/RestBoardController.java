package com.example.demo.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BoardVO;
import com.example.demo.model.BoardVO.BoardVOBuilder;
import com.example.demo.model.Result;
import com.example.demo.service.BoardService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/board/")
@RestController
public class RestBoardController {
	@Autowired
	BoardService service;
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="title", value="제목", required=true, dataType = "String"),
		@ApiImplicitParam(name="content", value="내용", required=true, dataType = "String"),
		@ApiImplicitParam(name="writerName", value="작성자이름", required=true, dataType = "String"),
		@ApiImplicitParam(name="writerEmail", value="작성자이메일", required=true, dataType = "String"),
	})
	@ApiOperation(value="글작성", notes="글작성", response=Result.class)
	@GetMapping(value="add")
	public Result add(Optional<String> title, Optional<String>content,
			Optional<String>writerName, Optional<String>writerEmail) {
		BoardVOBuilder builder = BoardVO.builder();
		if(title.isPresent()) {
			builder.title(title.get());
		}
		if(content.isPresent()) {
			builder.content(content.get());
		}
		if(writerName.isPresent()) {
			builder.writerName(writerName.get());
		}
		if(writerEmail.isPresent()) {
			builder.writerEmail(writerEmail.get());
		}
		BoardVO board = builder.build();
		
		Result result = Result.successInstance();
		int inserted = service.insert(board);
		result.setData(board);
		result.setTotalCount(inserted);
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="query", value="검색어", required=false, dataType = "String"),
		@ApiImplicitParam(name="pageNo", value="페이지번호", required=false, dataType = "int"),
	})
	@ApiOperation(value="게시판 글 가져오기", notes="1 페이지당 10개 가져온다.", response=Result.class)
	@GetMapping(value="list/{pageNo}")
	public Result selectAll(@RequestParam(value="query", required=false)Optional<String> query,
			@PathVariable(value="pageNo", required=false)Optional<Integer> pageNo) {
		BoardVOBuilder builder = BoardVO.builder();
		BoardVO vo = builder.build();
		if(pageNo.isPresent()) {
			vo.setPageNo(pageNo.get());
		}
		if(query.isPresent()) {
			vo.setQuery(query.get());
		}	
		
		Result result = Result.successInstance();
		List<BoardVO> list = service.select(vo);
		result.setData(list);
		result.setTotalCount(list.size());
		return result;
	}
	
	@GetMapping(value="/detail/{id}")
	public Result selectOne(@PathVariable(value="id", required=true)int id) {
		Result result = Result.successInstance();
		BoardVO vo = BoardVO.builder()
				.id(id).build();
		BoardVO selected = service.selectOne(vo);
		result.setData(selected);
		
		return result;
	}
	
	@GetMapping(value="/delete/{id}")
	public Result delete(@PathVariable(value="id", required=true)int id) {
		Result result = Result.successInstance();
		BoardVO vo = BoardVO.builder()
				.id(id).build();
		int deleted = service.delete(vo);
		result.setData(deleted);
		
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="아이디", required=true, dataType = "int"),
		@ApiImplicitParam(name="title", value="제목", required=false, dataType = "String"),
		@ApiImplicitParam(name="content", value="내용", required=false, dataType = "String"),
		@ApiImplicitParam(name="writerName", value="작성자이름", required=false, dataType = "String"),
		@ApiImplicitParam(name="writerEmail", value="작성자이메일", required=false, dataType = "String"),
	})
	@ApiOperation(value="글수정", notes="글수정", response=Result.class)
	@GetMapping(value="update")
	public Result update(int id,
			Optional<String> title, Optional<String>content,
			Optional<String>writerName, Optional<String>writerEmail) {
		BoardVOBuilder builder = BoardVO.builder();
		builder.id(id);
		if(title.isPresent()) {
			builder.title(title.get());
		}
		if(content.isPresent()) {
			builder.content(content.get());
		}
		if(writerName.isPresent()) {
			builder.writerName(writerName.get());
		}
		if(writerEmail.isPresent()) {
			builder.writerEmail(writerEmail.get());
		}
		BoardVO board = builder.build();
		
		Result result = Result.successInstance();
		int updated = service.update(board);
		result.setData(board);
		result.setTotalCount(updated);
		return result;
	}
}
