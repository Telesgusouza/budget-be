package com.example.demo.service.exception;

public class ResourceAlreadyExists extends RuntimeException {
	private static final long serialVersionUID = -7800991758452911251L;

	public ResourceAlreadyExists(Object obj) {
		super("Resource already exists");
	}

}
