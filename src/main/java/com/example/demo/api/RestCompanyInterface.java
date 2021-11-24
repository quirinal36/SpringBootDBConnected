package com.example.demo.api;

import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.CompanyVO;
import com.example.demo.model.Result;

public interface RestCompanyInterface {
	public Result findByCompanyName(@RequestParam(value="name")Optional<String>name);
	public Result findByCompanyNameExactly(@RequestParam(value="name")Optional<String>name);
	public Result findByCompanyId(@RequestParam(value="id")Optional<Integer>id);
	public Result insertCompany(CompanyVO input);
	public Result updateCompany(CompanyVO input);
}
