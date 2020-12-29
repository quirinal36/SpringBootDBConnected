package com.example.demo.control;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {

	private final String jwt;

	public AuthenticationResponse(String jwt) {
		this.jwt = jwt;
	}
	
}
