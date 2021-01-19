package com.example.demo.api;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BoardVO;
import com.example.demo.model.Result;
import com.example.demo.service.BoardService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RequestMapping(value="/api/v1/book")
@RestController
public class RestBoardController {
	@Autowired
	private BoardService service;
	
	/**
	 * 글 작성 API
	 * @param title
	 * @param content
	 * @param writer
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name="title", value="게시글 제목", required=true, dataType="String"),
		@ApiImplicitParam(name="content", value="게시글 내용", required=true, dataType="String"),
		@ApiImplicitParam(name="writer", value="작성자", required=true, dataType="Integer")
	})
	@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
	@ApiOperation(value="게시글 추가", notes="글 작성 API")
	@PostMapping(value="/add")
	public Result addBoard(@RequestParam(value="title", required=true)Optional<String> title, 
			@RequestParam(value="content", required=true)Optional<String> content,
			@RequestParam(value="writer", required=false)Optional<Integer> writer) {
		Result result = Result.successInstance();
		
		BoardVO board = new BoardVO();
		board.setTitle(title.get());
		board.setContent(content.get());
		board.setWriter(writer.get());
		
		int insertResult = service.insert(board);
		result.setData(insertResult);
		return result;
	}
	
	/**
	 * 게시글 전체 가져오기
	 * 
	 * @return
	 */
	@ApiOperation(value="게시글 리스트 가져오기", notes="모든 글 가져오기 API")
	@PostMapping(value="/select/all")
	public Result selectAll() {
		Result result = Result.successInstance();
		
		List<BoardVO> list = service.selectAll();
		result.setData(list);
		return result;
	}
}
