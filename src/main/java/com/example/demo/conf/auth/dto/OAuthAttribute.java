package com.example.demo.conf.auth.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.Builder;
import java.util.Map;

import com.example.demo.model.UserVO;
import com.example.demo.security.Role;

@ToString
@Getter
public class OAuthAttribute {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private int picture_id;
    
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
    			.roleType(Role.BUYER)
    			.build();
    }
}
