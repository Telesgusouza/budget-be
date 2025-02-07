package com.example.demo.service.exception;

public class AuthenticationFailed extends RuntimeException {
	private static final long serialVersionUID = 4818469451534424092L;

	public AuthenticationFailed(Object id) {
		super("Authentication Failed. id " + id);
	}

}
