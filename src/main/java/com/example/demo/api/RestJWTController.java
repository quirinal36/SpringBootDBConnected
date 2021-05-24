package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.control.AuthenticationRequest;
import com.example.demo.control.AuthenticationResponse;
import com.example.demo.exception.CommonException;
import com.example.demo.exception.EnumSecurityException;
import com.example.demo.model.JwtModel;
import com.example.demo.model.Result;
import com.example.demo.util.JwtUtil;

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
	public Result createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			JwtModel token = jwtTokenUtil.makeJwt(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			Result result = Result.successInstance();
			result.setData(new AuthenticationResponse(token));
			return result;
		}catch(CommonException e) {
			Result result = Result.failInstance();
			result.setMessage("비밀번호 불일치");
			return result;
		}catch(UsernameNotFoundException e) {
			Result result = Result.failInstance();
			result.setMessage("존재하지 않는 아이디");
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
	@ResponseBody
	public Result get_access_token() throws Exception{
		JwtModel token = this.jwtTokenUtil.makeReJwt();
//		return ResponseEntity.ok(new AuthenticationResponse(token));
		Result result = Result.successInstance();
		result.setData(new AuthenticationResponse(token));
		return result;
	}
}
