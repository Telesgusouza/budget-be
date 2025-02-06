package com.example.demo.resource.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.service.exception.ExceptionOfExistingEmail;
import com.example.demo.service.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException e,
			HttpServletRequest request) {

		String error = "Resource not found";
		Integer status = 400;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(ExceptionOfExistingEmail.class)
	public ResponseEntity<StandardError> handleExceptionOfExistingEmail(ExceptionOfExistingEmail e,
			HttpServletRequest request) {

		String error = "Email Already exists";
		Integer status = 422;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

}
