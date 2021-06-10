package com.example.demo.api;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/member/")
@RestController
public class RestCompanyController implements RestCompanyInterface{
	
	@Override
	public Result findByCompanyName(Optional<String> name) {
		// TODO Auto-generated method stub
		return null;
	}
}
