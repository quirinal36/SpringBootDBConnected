package com.example.demo.api;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.control.AuthenticationRequest;
import com.example.demo.control.AuthenticationResponse;
import com.example.demo.model.JwtModel;
import com.example.demo.model.Result;
import com.example.demo.model.UserVO;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;

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
	
	/**
	 * 유저 권한 변경하기
	 * @param id
	 * @param role
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="user id", required=true, dataType="int", defaultValue="0"),
		@ApiImplicitParam(name="role", value="authorization role", required=true, dataType="int", defaultValue="0")
	})
	@ApiOperation(value="관리자만 실행가능", notes="유저 권한변경", response=Result.class)
	@RolesAllowed("ROLE_ADMIN")
	@PostMapping(value="/member/grant")
	public Result grantAuth(@RequestParam(value="id", required=true, defaultValue="0", name="id")Optional<Integer> id, 
			@RequestParam(value="role", required=true, defaultValue="0", name="role")Optional<Integer> role) {
		Result result = Result.successInstance();
		
		UserVO user = new UserVO();
		user.setId(id.get());
		user.setRole(role.get());
		int updateResult = service.update(user);
		
		result.setData("result:"+updateResult);
		return result;
	}
	
	/**
	 * 모든 멤버 정보 가져오기
	 * @return
	 */
	@ApiOperation(value="모든 멤버들 정보 가져오기", notes="")
	@GetMapping(value="/member/info")
	public Result memberInfo() {
		Result result = Result.successInstance();
		result.setData(service.selectAll());
		return result;
	}
	
	/**
	 * 회원정보 수정하기
	 * @param id
	 * @param name
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="user id", required=true, dataType="int", defaultValue="0"),
		@ApiImplicitParam(name="name", value="user name", required=true, dataType="String")
	})
	@ApiOperation(value="User 정보 수정하기", notes="name 값만 수정 가능")
	@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
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
	
	/**
	 * 회원정보 추가하기 
	 * @param login
	 * @param password
	 * @param name
	 * @return
	 */
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
