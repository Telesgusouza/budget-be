package com.example.demo.resource.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.service.exception.AuthenticationFailed;
import com.example.demo.service.exception.EmailException;
import com.example.demo.service.exception.ErrorCreatingToken;
import com.example.demo.service.exception.ErrorLoggingIntoAccount;
import com.example.demo.service.exception.ErrorToken;
import com.example.demo.service.exception.ExceptionOfExistingEmail;
import com.example.demo.service.exception.InvalidField;
import com.example.demo.service.exception.ResourceAlreadyExists;
import com.example.demo.service.exception.ResourceNotFoundException;
import com.example.demo.service.exception.TicketError;

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

	@ExceptionHandler(ErrorCreatingToken.class)
	public ResponseEntity<StandardError> handleErrorCreatingToken(ErrorCreatingToken e, HttpServletRequest request) {

		String error = "Error creating token";
		Integer status = 400;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(ErrorLoggingIntoAccount.class)
	public ResponseEntity<StandardError> handleErrorLoggingIntoAccount(ErrorLoggingIntoAccount e,
			HttpServletRequest request) {

		String error = "Error Logging Into Account";
		Integer status = 400;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<StandardError> handleAuthenticationException(AuthenticationException e,
			HttpServletRequest request) {

		String error = "Authentication Exception";
		Integer status = 403;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(AuthenticationFailed.class)
	public ResponseEntity<StandardError> handleAuthenticationFailed(AuthenticationFailed e,
			HttpServletRequest request) {

		String error = "Error authenticating account";
		Integer status = 403;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(ErrorToken.class)
	public ResponseEntity<StandardError> handleErrorToken(ErrorToken e, HttpServletRequest request) {

		String error = "Error with Token";
		Integer status = 403;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(TicketError.class)
	public ResponseEntity<StandardError> handleTicketError(TicketError e, HttpServletRequest request) {

		String error = "Error with ticket";
		Integer status = 400;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(InvalidField.class)
	public ResponseEntity<StandardError> handleInvalidField(InvalidField e, HttpServletRequest request) {

		String error = "Invalid field";
		Integer status = 400;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);

	}

	@ExceptionHandler(EmailException.class)
	public ResponseEntity<StandardError> handleEmailException(EmailException e, HttpServletRequest request) {

		String error = "Email exception";
		Integer status = 500;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);

	}

	@ExceptionHandler(ResourceAlreadyExists.class)
	public ResponseEntity<StandardError> handleResourceAlreadyExists(EmailException e, HttpServletRequest request) {

		String error = "Resource already exists";
		Integer status = 409;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);

	}

}
