package com.example.demo.service.exception;

public class ExceptionOfExistingEmail extends RuntimeException {
	private static final long serialVersionUID = -3956935890410577749L;

	public ExceptionOfExistingEmail(Object id) {
		super("Exception Of Existing Email. id " + id);
	}
	
}
