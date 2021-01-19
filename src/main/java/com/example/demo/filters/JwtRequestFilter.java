package com.example.demo.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.exception.CommonException;
import com.example.demo.service.SolamonUserDetailsService;
import com.example.demo.util.HttpUtil;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.JwtUtil.TOKEN_TYPE;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	@Autowired
	private SolamonUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;
	
	private static final Pattern BEARER = Pattern.compile("Bearer", Pattern.CASE_INSENSITIVE);
	
	@Value("${jwt.get.access.token.url}")
	private String accessTokenUrl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;
		TOKEN_TYPE tokenType = TOKEN_TYPE.ACCESS_TOKEN;
		
		if(authorizationHeader!=null) {
			Matcher matcher = BEARER.matcher(authorizationHeader);
			if(matcher.find()) {
				jwt = authorizationHeader.substring(matcher.end() + 1);
				
				String requestURI = HttpUtil.getRequestURI(request);
				if(this.accessTokenUrl.equalsIgnoreCase(requestURI)) {
					tokenType = TOKEN_TYPE.REFRESH_TOKEN;
				}
				
				try {
					username = jwtUtil.extractUsername(jwt, tokenType);
				} catch (CommonException e) {
					e.printStackTrace();
				}
			}
		}
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			log.info("if start");
			// 사용자 아이디로 해당 정보가 있는지 확인함
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			log.info("user>>>>" + userDetails.getUsername());
			// 토큰의 유효성을 확인
			try {
				if(this.jwtUtil.validateToken( jwt, userDetails, tokenType)) {
					log.info("isValid success");
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails( request));
					log.info("token: " + token);
					// Security에 인증값 추가
					SecurityContextHolder.getContext().setAuthentication(token);					
				}
			} catch (CommonException e) {
				e.printStackTrace();
			}
		}
		filterChain.doFilter(request, response);
	}
}
