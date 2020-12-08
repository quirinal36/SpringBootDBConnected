package com.example.demo.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.BookInfo;
import com.example.demo.service.BookService;

@Controller
public class HomeController {
	@Autowired
	BookService service;
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public ModelAndView getView(ModelAndView mv) {
		List<BookInfo> list = service.selectAll();
		
		mv.addObject("list", list);
		
		mv.setViewName("/index.html");
		return mv;
	}
}
