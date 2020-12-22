package com.example.demo.service;

import java.util.Arrays;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserVO;

@Service
public class SolamonUserDetailsService implements UserDetailsService{
	@Autowired
	UserMapper mapper;
	final Logger logger = Logger.getLogger(SolamonUserDetailsService.class.getSimpleName());
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVO param = new UserVO();
		param.setLogin(username);
		UserVO user = mapper.selectUserByLogin(param);
		
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole_name());
		UserDetails userDetails = (UserDetails)new User(user.getLogin()
				,new String("{noop}"+user.getPassword()), Arrays.asList(authority));
		
		return userDetails;
	}
}
