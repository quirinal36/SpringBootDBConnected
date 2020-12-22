package com.example.demo.util;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class BongInterceptor implements HandlerInterceptor{
	Logger logger = Logger.getLogger(getClass().getSimpleName());
	@Autowired
	SqlSessionTemplate sqlSession;
	
//	private final JwtAuthToken
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("preHandle");
		
		Map<String, String[]> parameters = request.getParameterMap();
		Iterator<String> keys = parameters.keySet().iterator();
		while(keys.hasNext()) {
			final String key = keys.next();
			for(String value : parameters.get(key)) {
				logger.info("key : "+key + "|" + "value : "+value);
			}
		}
		
//		if(request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_ADMIN"))
//		{
//			logger.info("preHandle : isUserInRole_True");
//			return true;
//		} else {
//			logger.info("preHandle : isUserInRole_False");
//			response.sendRedirect(request.getContextPath() + "/login");
//			
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler,
			ModelAndView mv) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
