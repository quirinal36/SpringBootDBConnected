package com.example.demo.api;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CompanyVO;
import com.example.demo.model.Result;
import com.example.demo.service.CompanyService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/company/")
@RestController
public class RestCompanyController implements RestCompanyInterface{
	@Autowired
	private CompanyService service;
	
	@GetMapping("/find/name")
	@Override
	public Result findByCompanyName(@RequestParam(name="param", required=true) Optional<String> param) {
		Result result = Result.successInstance();
		if(param.isPresent()) {
			CompanyVO company = new CompanyVO();
			company.setName(param.get());
			List<CompanyVO> list = service.selectByName(company);
			result.setData(list);
			result.setTotalCount(list.size());
		}
		return result;
	}
	
	@GetMapping("/find/exist")
	@Override
	public Result findByCompanyNameExactly(@RequestParam(name="param", required=true) Optional<String> param) {
		Result result = Result.successInstance();
		if(param.isPresent()) {
			CompanyVO company = new CompanyVO();
			company.setName(param.get());
			CompanyVO list = service.selectByNameExactly(company);
			result.setData(list);
		}
		return result;
	}

	@GetMapping("/find/id/{id}")
	@Override
	public Result findByCompanyId(Optional<Integer> id) {
		Result result = Result.successInstance();
		CompanyVO company = new CompanyVO();
		company.setId(id.get());
		result.setData(service.selectOne(company));
		return result;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="name", value="company name", required=true, dataType="String"),
//		@ApiImplicitParam(name="reg_num", value="사업자번호", required=true, dataType="String"),
//		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@PostMapping("/add")
	@Override
	public Result insertCompany(CompanyVO input) {
		Result result = Result.successInstance();
		result.setTotalCount(service.insert(input));
		result.setData(input);
		return result;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="company id", required=true, dataType="Integer"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@PostMapping("/update")
	@Override
	public Result updateCompany(CompanyVO input) {
		Result result = Result.successInstance();
		result.setTotalCount(service.update(input));
		result.setData(input);
		return result;
	}
}
