package com.example.demo.api;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Result;
import com.example.demo.model.UserVO;
import com.example.demo.model.UserVO.UserVOBuilder;
import com.example.demo.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/member/")
@RestController
public class RestMemberController {
	
	@Autowired
	private UserService service;
	
	/**
	 * 유저 권한 변경하기
	 * @param id
	 * @param role
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name="user_id", value="user id", required=true, dataType="int", defaultValue="0"),
		@ApiImplicitParam(name="role", value="authorization role (1:ADMIN, 2:USER_SELLER, 3:USER_BUYER)", required=true, dataType="int", defaultValue="0"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header")
	})
	@ApiOperation(value="유저 권한변경", notes="관리자만 실행가능", response=Result.class)
	@RolesAllowed("ROLE_ADMIN")
	@PostMapping(value="/grant")
	public Result grantAuth(@RequestParam(value="user_id", required=true, defaultValue="0", name="user_id")Optional<Integer> userId, 
			@RequestParam(value="role", required=true, defaultValue="0", name="role")Optional<Integer> role) {
		Result result = Result.successInstance();
		
		UserVO user = new UserVO();
		if(userId.isPresent()) {
			user.setId(userId.get());
		}
		if(role.isPresent()) {
			user.setRole(role.get());
		}
		int updateResult = service.update(user);
		
		result.setData("result:"+updateResult);
		return result;
	}
	
	/**
	 * 모든 멤버 정보 가져오기
	 * @return
	 */
	@ApiOperation(value="모든 멤버들 정보 가져오기", notes="")
	@GetMapping(value="/info")
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
		@ApiImplicitParam(name="password", value="user password", required=false, dataType="String"),
		@ApiImplicitParam(name="name", value="user name", required=false, dataType="String"),
		@ApiImplicitParam(name="email", value="email 수정", required=false, dataType="String"),
		@ApiImplicitParam(name="phone", value="phone 수정", required=false, dataType="String"),
		@ApiImplicitParam(name="fax", value="fax 수정", required=false, dataType="String"),
		@ApiImplicitParam(name="mobile", value="mobile 수정", required=false, dataType="String"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header")
	})
	@ApiOperation(value="User 정보 수정하기", notes="name 값만 수정 가능")
	@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping(value="/edit")
	public Result memberEdit(@RequestParam(value="id", required=true, defaultValue="0", name="id")Optional<Integer> id,
			@RequestParam(value="password", required=false, name="password")Optional<String> password,
			@RequestParam(value="name", required=false, name="name")Optional<String> name,
			@RequestParam(value="email", required=false, name="email")Optional<String> email,
			@RequestParam(value="phone", required=false, name="phone")Optional<String> phone,
			@RequestParam(value="fax", required=false, name="fax")Optional<String> fax,
			@RequestParam(value="mobile", required=false, name="mobile")Optional<String> mobile) {
		UserVOBuilder user = UserVO.builder();
		user.id(id.get());
		if(password.isPresent()) {
			user.password(password.get());
		}
		if(name.isPresent()) {
			user.name(name.get());
		}
		if(email.isPresent()) {
			user.email(email.get());
		}
		if(phone.isPresent()) {
			user.phone(phone.get());
		}
		if(fax.isPresent()) {
			user.fax(fax.get());
		}
		if(mobile.isPresent()) {
			user.mobile(mobile.get());
		}
		
		Result result = Result.successInstance();
		result.setData(service.update(user.build()));
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
		@ApiImplicitParam(name="name", value="user name", required=false, dataType="String"),
		@ApiImplicitParam(name="email", value="user email", required=true, dataType="String"),
		@ApiImplicitParam(name="phone", value="user phone", required=false, dataType="String"),
		@ApiImplicitParam(name="fax", value="user fax", required=false, dataType="String"),
		@ApiImplicitParam(name="mobile", value="user mobile", required=false, dataType="String"),
		@ApiImplicitParam(name="pictureId", value="user picture", required=false, dataType="int", defaultValue="0"),
		@ApiImplicitParam(name="companyId", value="user company", required=false, dataType="int", defaultValue="0")
	})
	@ApiOperation(value="회원정보 추가", notes="user 정보를 입력")
	@PostMapping(value="/add")
	public Result addUser(@RequestParam(name="login")Optional<String>login,
			@RequestParam(name="password")Optional<String>password,
			@RequestParam(name="name", required=false)Optional<String>name,
			@RequestParam(name="email", required=true)Optional<String>email,
			@RequestParam(name="phone", required=false)Optional<String>phone,
			@RequestParam(name="fax", required=false)Optional<String>fax,
			@RequestParam(name="mobile", required=false)Optional<String>mobile,
			@RequestParam(name="pictureId", required=false)Optional<Integer> picture,
			@RequestParam(name="companyId", required=false)Optional<Integer> company) {
		Result result = Result.successInstance();
		UserVOBuilder user = UserVO.builder();
		
		user.login(login.get())
			.password(password.get());
		if(name.isPresent()) user.name(name.get());
		if(email.isPresent()) user.email(email.get());
		if(phone.isPresent()) user.phone(phone.get());
		if(fax.isPresent()) user.fax(fax.get());
		if(mobile.isPresent()) user.mobile(mobile.get());
		if(picture.isPresent()) {
			user.pictureId(picture.get());
		}else {
			user.pictureId(1);
		}
		if(company.isPresent()) {
			user.companyId(company.get());
		}else {
			user.companyId(1);
		}
		
		int insertResult = service.insert(user.build());
		result.setData(insertResult);
		return result;
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name="login", value="user login", required=true, dataType="String")
	})
	@ApiOperation(value="회원 아이디 존재여부 판단", notes="login 정보가 중복된게 있는지 찾아 알려준다. data 가 0일 때 아이디를 만들 수 있다.")
	@GetMapping(value="/exist")
	public Result isExistUser(@RequestParam(value="login", required=true)Optional<String> login) {
		Result result = Result.successInstance();
		UserVO user = new UserVO();
		if(login.isPresent()) {
			user.setLogin(login.get());
			UserVO selected = service.selectOne(user);
			if(selected != null && selected.getId()>0) {
				result.setData(selected.getId());
			}else {
				result.setData(0);
			}
		}
		return result;
	}

	/**
	 * email 정보를 활용해 로그인 정보 받아오기
	 * 
	 * @param email
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name="email", value="user email", required=true, dataType="String")
	})
	@ApiOperation(value="회원 아이디 찾기", notes="email 정보를 이용해 아이디를 검색한다.")
	@GetMapping(value="/find/login")
	public Result findLogin(@RequestParam(value="email", required=true)Optional<String> email) {
		Result result = Result.successInstance();
		UserVO user = UserVO.builder()
				.email(email.get()).build();
		UserVO selectedUserByEmail = service.selectUserByEmail(user.getEmail());
		if(selectedUserByEmail != null) {
			result.setData(selectedUserByEmail);
		}else {
			result.setData(-1);
			result.setMessage("잘못된 이메일 주소를 입력하셨습니다.");
		}
		return result;
	}
	/**
	 * 비밀번호 찾기 기능을 이용하기 위한 관문
	 * 
	 * @param login
	 * @param email
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name="login", value="user login", required=true, dataType="String"),
		@ApiImplicitParam(name="email", value="user email", required=true, dataType="String")
	})
	@ApiOperation(value="회원 비밀번호 변경화면으로 이동", notes="Login 정보와 email 정보를 이용해 비밀번호 변경 화면으로 이동한다.")
	@GetMapping(value="/redirect/password")
	public Result findPassword(@RequestParam(value="login", required=true)Optional<String> login,
			@RequestParam(value="email", required=true)Optional<String> email) {
		Result result = Result.successInstance();
		UserVO user = UserVO.builder()
				.login(login.get())
				.email(email.get()).build();
		UserVO selectedUserByLogin = service.selectUserByLogin(user);
		UserVO selectedUserByEmail = service.selectUserByEmail(user.getEmail());
		
		if(selectedUserByLogin != null && selectedUserByLogin.getId()>0 &&
				selectedUserByEmail != null && selectedUserByEmail.getId() > 0) {
			result.setData(1);
		}else if(selectedUserByLogin == null || selectedUserByLogin.getId() <= 0){
			result.setData(-1);
			result.setMessage("잘못된 아이디를 입력하셨습니다.");
		}else if(selectedUserByEmail == null || selectedUserByEmail.getId() <= 0){
			result.setData(-1);
			result.setMessage("잘못된 이메일 주소를 입력하셨습니다.");
		}else {
			result.setData(0);
			result.setMessage("아이디와 이메일주소를 다시한번 확인 해주세요.");
		}
		return result;
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="user id", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@ApiOperation(value="회원 정보 가져오기", notes="특정한 1명의 유저 정보를 보여준다.")
	@PostMapping(value="/get/{id}")
	public Result getUserById(@PathVariable(value="id")Integer id) {
		Result result = Result.successInstance();
		UserVO user = UserVO.newInstanse(id);
		UserVO selected = service.selectById(user);
		if(selected != null && selected.getId()>0) {
			result.setData(selected);
		}else {
			result.setData(null);
			result.setMessage("회원정보를 가져오는 데 실패하였습니다.");
		}
		return result;
	}
}
