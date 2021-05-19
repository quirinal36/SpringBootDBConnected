package com.example.demo.security;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Role {
	ADMIN("ROLE_ADMIN", "관리자권한"),
    SELLER("ROLE_SELLER", "판매자권한"),
    BUYER("ROLE_BUYER", "구매자권한");

    private String code;
    private String description;

    Role(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public static Role of(String code) {
        return Arrays.stream(Role.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(BUYER);
    }
}
