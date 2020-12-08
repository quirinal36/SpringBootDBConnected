package com.example.demo.control;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GalaxyController {

	@RequestMapping(value="/api", method =RequestMethod.GET)
	public String getApi() {
		JSONObject json = new JSONObject();
		json.put("result", 1);
		
		return json.toString();
	}
}
