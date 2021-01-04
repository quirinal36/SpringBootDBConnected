package com.example.demo.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.control.AuthenticationRequest;
import com.example.demo.control.AuthenticationResponse;
import com.example.demo.model.Result;
import com.example.demo.model.UserVO;
import com.example.demo.service.SolamonUserDetailsService;
import com.example.demo.service.UserService;
import com.example.demo.service.WorkLinksService;
import com.example.demo.service.jwt.JwtService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.RedisUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1")
@RestController
public class RestUserController {
	
	@Autowired
	UserService service;
	
	@Autowired
	WorkLinksService workService;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private SolamonUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	RedisUtil redisUtil;
	
	/*
	@ApiImplicitParams({
		@ApiImplicitParam(name="TOKEN", value="로그인 성공 후 access token, refresh token", required=true)
	})
	*/
	@ApiOperation(value="auth key 발급", notes="jwt key 발급")
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		final int accessTokenDuration = 1000 * 60 * 30;
		final int refreshTokenDuration = 1000 * 60 * 60 * 10;
		
//		String who = redisUtil.getData(authenticationRequest.getUsername());
//		log.info("who:" + who);
		
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}catch(BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String accessToken = jwtTokenUtil.generateToken(userDetails, accessTokenDuration);
		final String refreshToken = jwtTokenUtil.generateToken(userDetails, refreshTokenDuration);
		redisUtil.setData(authenticationRequest.getUsername(), refreshToken, refreshTokenDuration);
		
		return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
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
//	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value="/member/info")
	public Result memberInfo(UserVO user) {
		Result result = Result.successInstance();
		result.setData("hello");
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping(value="/list/links")
	public Result linkList() {
		Result result = Result.successInstance();
		result.setData(workService.selectAll());
		return result;
	}
}
