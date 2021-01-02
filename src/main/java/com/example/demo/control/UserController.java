package com.example.demo.control;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Result;
import com.example.demo.model.UserVO;
import com.example.demo.service.UserService;
import com.example.demo.service.jwt.JwtService;
import com.example.demo.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {
	@Autowired
	UserService service;
	@Autowired
	JwtService jwtService;
	@Autowired
	RedisUtil redisUtil;
	
	@GetMapping(value="/login")
	public ModelAndView loginView(ModelAndView mv) {
		mv.setViewName("/login.html");
		//redisUtil.setData("iam", "stranger");
		String who = redisUtil.getData("iam");
		log.info("who: " + who);
		return mv;
	}
	
	@GetMapping(value="/signup")
	public ModelAndView signupView(ModelAndView mv) {
		mv.setViewName("/signup.html");
		return mv;
	}
	@ResponseBody
	@PostMapping(value="/signup")
	public String signup(UserVO input) {
		log.info(input.toString());
		
		JSONObject json = new JSONObject();
		json.put("result", service.insert(input));
		return json.toString();
	}
}
