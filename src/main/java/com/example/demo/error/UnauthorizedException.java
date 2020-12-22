package com.example.demo.error;

public class UnauthorizedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8023595755549406965L;
	public UnauthorizedException() {
		super("계정 권한이 유효하지 않습니다. 다시 로그인 해주세요.");
	}
}
