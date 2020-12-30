package com.example.demo.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.control.AuthenticationRequest;
import com.example.demo.control.AuthenticationResponse;
import com.example.demo.model.Result;
import com.example.demo.model.UserVO;
import com.example.demo.service.SolamonUserDetailsService;
import com.example.demo.service.UserService;
import com.example.demo.service.jwt.JwtService;
import com.example.demo.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1")
@RestController
public class RestUserController {
	
	@Autowired
	UserService service;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private SolamonUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		log.info(authenticationRequest.getUsername());
		log.info(authenticationRequest.getPassword());
		
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}catch(BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
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
		result.setData("hello");
		return result;
	}
}
