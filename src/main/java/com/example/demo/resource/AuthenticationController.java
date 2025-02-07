package com.example.demo.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.ResponseTokenDTO;
import com.example.demo.resource.exception.StandardError;
import com.example.demo.service.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(
		name = "Authentication",
		description = "Contains operations related to the login and registration stage")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	private AuthorizationService authorizationService;

	@Operation(
			summary = "log into your account",
			description = "Log user into your account",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "User logged in successfully",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ResponseTokenDTO.class))),
					@ApiResponse(
							responseCode = "403",
							description = "Error authenticating account",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "403",
							description = "Incorrect password",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "403",
							description = "Account deactivated or blocked",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "403",
							description = "Error while generating token.",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class))),
					
					
			})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {

		String token = authorizationService.login(data);

		return ResponseEntity.status(200).body(new ResponseTokenDTO(token));
	}

	@Operation(
			summary = "Create an account",
			description = "Register your account",
			responses = {
					@ApiResponse(
							responseCode = "201",
							description = "User successfully registered",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ResponseTokenDTO.class))),
					
					@ApiResponse(
							responseCode = "422",
							description = "User already exists",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "Error while generating token.",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class))),
					
			})
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {

		String token = authorizationService.register(data);

		return ResponseEntity.status(201).body(new ResponseTokenDTO(token));
	}

}
