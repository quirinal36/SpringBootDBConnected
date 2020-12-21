package com.example.demo.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.WorkLinks;
import com.example.demo.service.WorkLinksService;

@Controller
public class HomeController {
	@Autowired
	WorkLinksService service;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getView(ModelAndView mv) {
		List<WorkLinks> list = service.selectAll();
		
		mv.addObject("list", list);
		
		mv.setViewName("/index.html");
		return mv;
	}
}
