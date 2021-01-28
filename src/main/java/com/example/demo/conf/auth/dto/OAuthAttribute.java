package com.example.demo.conf.auth.dto;

import lombok.Getter;
import lombok.Builder;
import java.util.Map;

import com.example.demo.model.Role;
import com.example.demo.model.UserVO;

@Getter
public class OAuthAttribute {
    private Map<String, Object> attributes;
    private String nameAttributeKey, name, email, picture;
    @Builder
    public OAuthAttribute(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
    public static OAuthAttribute of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }
    public static OAuthAttribute ofGoogle(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public UserVO toEntity() {
    	return UserVO.builder()
    			.name(name)
    			.email(email)
    			.picture(picture)
    			.roleType(Role.USER)
    			.build();
    }
}
