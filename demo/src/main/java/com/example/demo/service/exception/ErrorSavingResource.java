package com.example.demo.service.exception;

public class ErrorSavingResource extends RuntimeException {
	private static final long serialVersionUID = 147636160570583016L;

	public ErrorSavingResource(Object id) {
		super("Error Saving Resource. id " + id);
	}
	
}
