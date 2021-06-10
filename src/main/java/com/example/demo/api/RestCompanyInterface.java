package com.example.demo.api;

import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Result;

public interface RestCompanyInterface {
	public Result findByCompanyName(@RequestParam(value="name")Optional<String>name);
}
