package com.example.demo.control;

import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.UserVO;
import com.example.demo.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserService service;
	final Logger logger = Logger.getLogger(UserController.class.getSimpleName());
	
	@GetMapping(value="/login")
	public ModelAndView loginView(ModelAndView mv) {
		mv.setViewName("/login.html");
		return mv;
	}
	
	@GetMapping(value="/signup")
	public ModelAndView signupView(ModelAndView mv) {
		mv.setViewName("/signup.html");
		return mv;
	}
	@ResponseBody
	@PostMapping(value="/signup")
	public String signup(UserVO input) {
		logger.info(input.toString());
		
		JSONObject json = new JSONObject();
		json.put("result", service.insert(input));
		return json.toString();
	}
}
