package com.example.demo.service.exception;

public class ErrorToken extends RuntimeException {
	private static final long serialVersionUID = -8830285955785386726L;

	public ErrorToken(Object id) {
		super("Error with Token. id " + id);
	}
	
}
