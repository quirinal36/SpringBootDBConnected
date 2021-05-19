package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.control.AuthenticationRequest;
import com.example.demo.control.AuthenticationResponse;
import com.example.demo.model.JwtModel;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;

import io.swagger.annotations.ApiOperation;
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
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		JwtModel token = jwtTokenUtil.makeJwt(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	/**
	 * refresh token을 이용해 access token 과 refresh token 재발급
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="refresh token을 이용해 access token 과 refresh token 재발급", notes="jwt key 재발급")
	@RequestMapping( value = "/get_access_token", method = RequestMethod.POST)
	public ResponseEntity<?> get_access_token() throws Exception{
		JwtModel token = this.jwtTokenUtil.makeReJwt();
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
}
