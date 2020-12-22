package com.example.demo.util;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.error.CustomAuthenticationException;
import com.example.demo.security.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.example.demo.security.SecurityConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BongInterceptor implements HandlerInterceptor{
	@Autowired
	SqlSessionTemplate sqlSession;
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler)
			throws Exception {
		log.info("preHandle");
		if(request.getSession().getAttribute(KEY_ROLE) != null && request.getSession().getAttribute(KEY_ROLE).equals(Role.USER.name())) {
			return true;
		}else {
			throw new CustomAuthenticationException();
		}
		/*
		Map<String, String[]> parameters = request.getParameterMap();
		Iterator<String> keys = parameters.keySet().iterator();
		while(keys.hasNext()) {
			final String key = keys.next();
			for(String value : parameters.get(key)) {
				log.info("key : "+key + "|" + "value : "+value);
			}
		}
		*/
//		if(request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_ADMIN"))
//		{
//			logger.info("preHandle : isUserInRole_True");
//			return true;
//		} else {
//			logger.info("preHandle : isUserInRole_False");
//			response.sendRedirect(request.getContextPath() + "/login");
//			
//		}
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
