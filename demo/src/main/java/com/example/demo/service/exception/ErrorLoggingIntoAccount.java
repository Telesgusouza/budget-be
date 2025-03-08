package com.example.demo.service.exception;

public class ErrorLoggingIntoAccount extends RuntimeException {
	private static final long serialVersionUID = -9136581932464461984L;

	public ErrorLoggingIntoAccount(Object id) {
		super("Error Logging Into Account. id " + id);
	}

}
