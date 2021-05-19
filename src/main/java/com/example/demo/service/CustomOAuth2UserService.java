package com.example.demo.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.conf.auth.dto.OAuthAttribute;
import com.example.demo.model.UserVO;
import com.example.demo.security.Role;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	@Autowired
	private UserService service;
	
	@Value("${jwt.pass.token.secure.key}")
	private String OAUTH_PASS;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
	    OAuth2User oAuth2User = delegate.loadUser(userRequest);
	    // 현재 로그인 진행 중인 서비스를 구분하는 코드
	    String registrationId = userRequest
	            .getClientRegistration()
	            .getRegistrationId();
	    // oauth2 로그인 진행 시 키가 되는 필드값
	    String userNameAttributeName = userRequest.getClientRegistration()
	            .getProviderDetails()
	            .getUserInfoEndpoint()
	            .getUserNameAttributeName();
	    // OAuthAttributes: attribute를 담을 클래스 (개발자가 생성)
	    OAuthAttribute attributes = OAuthAttribute
	            .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
	    log.info(">>>>>>>>>>>>>>"  + attributes.getEmail());
	    UserVO user = saveOrUpdate(attributes);
	    // SessioUser: 세션에 사용자 정보를 저장하기 위한 DTO 클래스 (개발자가 생성)
	    // httpSession.setAttribute("user", new SessionUser(user));
	    return new DefaultOAuth2User(
	            Collections.singleton(new SimpleGrantedAuthority(Role.BUYER.getCode())),
	            attributes.getAttributes(),
	            attributes.getNameAttributeKey()
	    );
	}
	private UserVO saveOrUpdate(OAuthAttribute attribute) {
		if(attribute!=null) {
			log.info("attr: "+attribute.toString());
		}
		
		UserVO user = service.selectUserByEmail(attribute.getEmail());
		log.info("______________saveOrUpdate");
		if(user!=null) {
			user = UserVO.builder()
					.id(user.getId())
					.roleType(user.getRoleType())
					.name(attribute.getName())
					.picture(attribute.getPicture()).build();
			service.update(user);
		}else {
			user = UserVO.builder()
					.login(attribute.getEmail())
					.roleType(Role.BUYER)
					.password(OAUTH_PASS)
					.email(attribute.getEmail())
					.name(attribute.getName())
					.picture(attribute.getPicture()).build();
			service.insert(user);
		}
		return user;
	}
}
