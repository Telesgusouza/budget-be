package com.example.demo.service.exception;

public class ErrorCreatingToken extends RuntimeException {
	private static final long serialVersionUID = 392896542845126172L;

	public ErrorCreatingToken(Object id) {
		super("Error Creating Token. id " + id);
	}

}
