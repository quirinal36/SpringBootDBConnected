package com.example.demo.control;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Result;
import com.example.demo.model.UserVO;
import com.example.demo.service.UserService;
import com.example.demo.service.jwt.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1")
@RestController
public class RestUserController {
	
	@Autowired
	UserService service;
	
	@Autowired
	JwtService jwtService;
	
	@PostMapping(value="/signup")
	public Result signUp(UserVO user, HttpServletResponse response) {
		log.info(user.toString());
		Result result = Result.successInstance();
		service.insert(user);
		result.setData(user);
		
		return result;
	}
	@PostMapping(value="/login")
	public Result signIn(UserVO user, HttpServletResponse response) {
		log.debug(user.toString());
		
		Result result = Result.successInstance();
		user = service.selectUserByLogin(user);
		if(user!= null) {
			String token = jwtService.create("member", user, "user");
			response.setHeader(UserVO.AUTH, token);
			result.setData(user);
		}
		
		return result;
	}
	
	@GetMapping(value="/member/info")
	public Result memberInfo(UserVO user) {
		Result result = Result.successInstance();
		
		return result;
	}
}
