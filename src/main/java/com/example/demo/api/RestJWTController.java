package com.example.demo.api;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.control.AuthenticationRequest;
import com.example.demo.control.AuthenticationResponse;
import com.example.demo.exception.CommonException;
import com.example.demo.model.JwtModel;
import com.example.demo.model.Result;
import com.example.demo.model.UserVO;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.JwtUtil.TOKEN_TYPE;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/")
@RestController
public class RestJWTController {
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private UserService userService;
	
	/**
	 * login, password 값을 활용해 access token, refresh token 값 발급
	 * @param authenticationRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="auth key 발급", notes="jwt key 발급")
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	@ApiResponses({
		@ApiResponse(code=401, message="인증실패"),
		@ApiResponse(code=500, message="존재하지 않는 아이디")
	})
	@ResponseBody
	public Result createAuthenticationToken(
			@RequestBody AuthenticationRequest authenticationRequest) {
		log.info(authenticationRequest.getUsername());
		
		try {
			JwtModel token = jwtTokenUtil.makeJwt(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			Result result = Result.successInstance();
			result.setData(new AuthenticationResponse(token));
			return result;
		}catch(CommonException e) {
			Result result = Result.failInstance();
			result.setMessage("비밀번호가 일치하지 않습니다.");
			result.setStatusCode(HttpStatus.UNAUTHORIZED);
			return result;
		}catch(UsernameNotFoundException e) {
			Result result = Result.failInstance();
			result.setStatusCode(HttpStatus.UNAUTHORIZED);
			result.setMessage("존재하지 않는 아이디입니다.");
			return result;
		}
	}
	
	/**
	 * refresh token을 이용해 access token 과 refresh token 재발급
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="refresh token을 이용해 access token 과 refresh token 재발급", notes="jwt key 재발급")
	@RequestMapping( value = "/get_access_token", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header")
	})
	@ApiResponses({
		@ApiResponse(code=400, message="인증실패"),
		@ApiResponse(code=401, message="인증실패"),
	})
	@ResponseBody
	public Result get_access_token() throws Exception{
		JwtModel token = this.jwtTokenUtil.makeReJwt();
		Result result = Result.successInstance();
		result.setData(new AuthenticationResponse(token));
		return result;
	}
	
	@ApiOperation(value="access token을 이용해 user 정보 돌려주기")
	@RequestMapping( value = "/getUserInfo", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header")
	})
	@ApiResponses({
		@ApiResponse(code=400, message="인증실패"),
		@ApiResponse(code=401, message="인증실패"),
	})
	@ResponseBody
	public Result getUserInfo(
			HttpServletRequest request) throws Exception{
		String authorizationHeader = request.getHeader("Authorization");
		log.info("authorizationHeader: "+authorizationHeader);
//		authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTYyNTQ4MzYxMSwiaWF0IjoxNjI1NDgxODExfQ.bbS7aSs2Hz1EtQpBFeTn5V8MoNfTevCnZAujnLMZ7Hk";
		
		final Pattern BEARER = Pattern.compile("Bearer", Pattern.CASE_INSENSITIVE);
		Matcher matcher = BEARER.matcher(authorizationHeader);
		if(matcher.find()) {
			String jwt = authorizationHeader.substring(matcher.end() + 1);
			String login = jwtTokenUtil.extractUsername(jwt, TOKEN_TYPE.ACCESS_TOKEN);
			log.info("login: " + login);
			
			UserVO user = UserVO.builder().login(login).build();
			
			user = userService.selectUserByLogin(user);
			
			Result result = Result.successInstance();
			result.setData(user);
			return result;
		}else {
			log.info("match failed");
			Result result = Result.failInstance();
			return result;
		}
		
	}
}
