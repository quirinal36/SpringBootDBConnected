package com.example.demo.control;

import com.example.demo.model.JwtModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
	private JwtModel jwt;

	public AuthenticationResponse(JwtModel jwt) {
		super();
		this.jwt = jwt;
	}
	
}
