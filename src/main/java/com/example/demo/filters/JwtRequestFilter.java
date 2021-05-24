package com.example.demo.filters;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
		log.info("doFilterInternal --------- start");
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
					log.info(e.getMessage());
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// 사용자 아이디로 해당 정보가 있는지 확인함
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			// 토큰의 유효성을 확인
			try {
				if(this.jwtUtil.validateToken( jwt, userDetails, tokenType)) {
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails( request));
					// Security에 인증값 추가
					SecurityContextHolder.getContext().setAuthentication(token);					
				}
			} catch (CommonException e) {
				log.info(e.getMessage());
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		filterChain.doFilter(request, response);
	}
}
