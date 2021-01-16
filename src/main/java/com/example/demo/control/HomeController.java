package com.example.demo.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	
	@Value("${spring.profile.value}")
	private String profile;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getView(ModelAndView mv) {
		//List<WorkLinks> list = service.selectAll();
		
		//mv.addObject("list", list);
		mv.addObject("profile", profile);
		
		mv.setViewName("redirect:/swagger-ui/");
		return mv;
	}
}
