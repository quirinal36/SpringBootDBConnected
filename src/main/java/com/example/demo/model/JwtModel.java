package com.example.demo.model;

public class JwtModel {

	private final String accessToken;
	private final String refreshToken;

	public JwtModel(String accessToken, String refreshToken) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

}
