package com.example.demo.control;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.WorkLinks;
import com.example.demo.service.WorkLinksService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	@Autowired
	WorkLinksService service;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getView(ModelAndView mv) {
		List<WorkLinks> list = service.selectAll();
		
		mv.addObject("list", list);
		
		mv.setViewName("/index.html");
		return mv;
	}
	
	@GetMapping(value="/rest/home")
	@ResponseBody
	public String home() {
		JSONObject json = new JSONObject();
		json.put("result", service.selectAll());
		return json.toString();
	}
	
//	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value="/home")
	public ModelAndView homeView(ModelAndView mv) {
		mv.setViewName("/home.html");
		return mv;
	}
	
	@GetMapping(value="/swagger-ui")
	public ModelAndView swaggerUiView(ModelAndView mv, HttpServletRequest req) throws MalformedURLException {
		URL url = new URL(req.getRequestURL().toString());
		StringBuilder targetUrl = new StringBuilder()
					.append(url.getProtocol()).append("://")
					.append(url.getHost());
		if(url.getPort() > 0) {
			targetUrl.append(":").append(url.getPort());
		}
		targetUrl.append("/v2/api-docs");
		log.info(targetUrl.toString());
		
		String result = restTemplate.getForObject(targetUrl.toString(), String.class);
		JSONObject json = new JSONObject(result);
		
		mv.addObject("json", json);
		mv.setViewName("/swagger-ui");
		return mv;
	}
}
