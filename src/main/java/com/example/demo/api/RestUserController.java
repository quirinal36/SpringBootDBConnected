package com.example.demo.api;

import java.util.Optional;

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
	private UserService service;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SolamonUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@ApiOperation(value="auth key 발급", notes="jwt key 발급")
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		final int accessTokenDuration = 1000 * 60 * 30;
		final int refreshTokenDuration = 1000 * 60 * 60 * 10;
		
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
	
//	public ResponseEntity<?> 
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="user id", required=true, dataType="int", defaultValue="0"),
		@ApiImplicitParam(name="role", value="authorization role", required=true, dataType="int", defaultValue="0")
	})
	@ApiOperation(value="관리자만 실행가능", notes="유저 권한변경", response=Result.class)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value="/member/grant")
	public Result grantAuth(@RequestParam(value="id", required=true, defaultValue="0", name="id")Optional<Integer> id, 
			@RequestParam(value="role", required=true, defaultValue="0", name="role")Optional<Integer> role) {
		Result result = Result.successInstance();
		
		UserVO user = new UserVO();
		user.setId(id.get());
		user.setRole(role.get());
		log.info(user.toString());
		int updateResult = service.update(user);
		
		result.setData("result:"+updateResult);
		return result;
	}
	
	@ApiOperation(value="모든 멤버들 정보 가져오기", notes="")
	@GetMapping(value="/member/info")
	public Result memberInfo() {
		Result result = Result.successInstance();
		result.setData(service.selectAll());
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="user id", required=true, dataType="int", defaultValue="0"),
		@ApiImplicitParam(name="name", value="user name", required=true, dataType="String")
	})
	@ApiOperation(value="User 정보 수정하기", notes="name 값만 수정 가능")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping(value="/member/edit")
	public Result memberEdit(@RequestParam(value="id", required=true, defaultValue="0", name="id")Optional<Integer> id,
			@RequestParam(value="name", required=true, name="name")Optional<String> name) {
		UserVO user = new UserVO();
		user.setId(id.get());
		user.setName(name.get());
		
		Result result = Result.successInstance();
		result.setData(service.update(user));
		return result;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="login", value="user login", required=true, dataType="String"),
		@ApiImplicitParam(name="password", value="user password", required=true, dataType="String"),
		@ApiImplicitParam(name="name", value="user name", required=false, dataType="String")
	})
	@ApiOperation(value="회원정보 추가", notes="login, password, name 정보를 입력한다.")
	@PostMapping(value="/user/add")
	public Result addUser(@RequestParam(value="login", required=true)Optional<String> login, 
			@RequestParam(value="password", required=true)Optional<String> password,
			@RequestParam(value="name", required=false)Optional<String> name) {
		Result result = Result.successInstance();
		
		UserVO user = new UserVO();
		user.setLogin(login.get());
		user.setPassword(password.get());
		user.setName(name.get());
		
		int insertResult = service.insert(user);
		result.setData(insertResult);
		return result;
	}
	
	
}
