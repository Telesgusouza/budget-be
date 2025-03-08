package com.example.demo.service.exception;

public class InvalidField extends RuntimeException {
	private static final long serialVersionUID = -1641621009306316354L;

	public InvalidField(Object id) {
		super("Invalid Field. id " + id);
	}
	
}
