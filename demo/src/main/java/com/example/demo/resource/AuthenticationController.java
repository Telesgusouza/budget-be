package com.example.demo.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.dto.EditPasswordDTO;
import com.example.demo.dto.EditUserDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.ResponseTokenDTO;
import com.example.demo.dto.ResponseUserDTO;
import com.example.demo.entity.Mail;
import com.example.demo.entity.User;
import com.example.demo.resource.exception.StandardError;
import com.example.demo.service.AuthorizationService;
import com.example.demo.service.EmailService;
import com.example.demo.service.S3Service;
import com.example.demo.service.exception.AuthenticationFailed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Contains operations related to the login and registration stage")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private S3Service s3Service;

	@Operation(summary = "log into your account", description = "Log user into your account", responses = {

			@ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTokenDTO.class))),

			@ApiResponse(responseCode = "403", description = "Error authenticating account", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "403", description = "Incorrect password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "403", description = "Account deactivated or blocked", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "403", description = "Error while generating token.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {

		String token = authorizationService.login(data);

		return ResponseEntity.status(200).body(new ResponseTokenDTO(token));
	}

	@Operation(summary = "Create an account", description = "Register your account", responses = {
			@ApiResponse(responseCode = "201", description = "User successfully registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTokenDTO.class))),

			@ApiResponse(responseCode = "422", description = "User already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "Error while generating token.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

	})
	@PostMapping("/register")
	public ResponseEntity<ResponseTokenDTO> register(@RequestBody @Valid RegisterDTO data) {

		String token = authorizationService.register(data);

		return ResponseEntity.status(201).body(new ResponseTokenDTO(token));
	}

	@Operation(summary = "get info user", description = "bring basic user information", responses = {
			@ApiResponse(responseCode = "200", description = "data recovered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDTO.class))),

			@ApiResponse(responseCode = "403", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))

	})
	@GetMapping
	public ResponseEntity<ResponseUserDTO> getUser(@AuthenticationPrincipal User user) {

		if (user == null) {
			throw new AuthenticationFailed("User not found");
		}

		ResponseUserDTO responseUser = new ResponseUserDTO(user.getId(), user.getImg(), user.getUsername(),
				user.getName());

		return ResponseEntity.status(200).body(responseUser);
	}

	@GetMapping("/all-info")
	public ResponseEntity<User> getAllInfoUser(@AuthenticationPrincipal User user) {

		return ResponseEntity.ok().body(user);
	}

//	@Operation(
//			summary = "get all info user",
//			description = "bring all user information",
//			responses = {
//					@ApiResponse(
//							responseCode = "200",
//							description = "data recovered successfully",
//							content = @Content(
//									mediaType = "application/json",
//									schema = @Schema(implementation = ResponseAllInfoUserDTO.class))
//							),
//					
//					@ApiResponse(
//							responseCode = "403",
//							description = "User not found",
//							content = @Content(
//									mediaType = "application/json",
//									schema = @Schema(implementation = StandardError.class))
//							)
//					
//					
//					
//			}
//			)
//	@GetMapping("/all-info")
//	public ResponseEntity<ResponseAllInfoUserDTO> getAllInfoUser(@AuthenticationPrincipal User user) {
//
//		if (user == null) {
//			throw new AuthenticationFailed("User not found");
//		}
//
//		ResponseAllInfoUserDTO responseUser = new ResponseAllInfoUserDTO(user.getId(), user.getImg(),
//				user.getUsername(), user.getName(),
//
//				 user.getTransaction(), user.getPots(), user.getAccount(), user.getBudget());
//		return ResponseEntity.status(200).body(responseUser);
//	}

	@Operation(summary = "delete user", description = "operation that deletes the user account", responses = {
			@ApiResponse(responseCode = "204", description = "account deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),

			@ApiResponse(responseCode = "400", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "error when deleting user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))

	})
	@DeleteMapping
	public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal User user) {

		this.authorizationService.deleteAccount(user.getId());

		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Edit user", description = "operation to edit the user name", responses = {
			@ApiResponse(responseCode = "200", description = "operation to edit the user name", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDTO.class))),

			@ApiResponse(responseCode = "400", description = "name cannot be the same as the previous name", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),

			@ApiResponse(responseCode = "400", description = "name is not null", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))), })
	@PatchMapping
	public ResponseEntity<ResponseUserDTO> editUser(@RequestBody EditUserDTO data, @AuthenticationPrincipal User user) {

		ResponseUserDTO response = this.authorizationService.editUser(data, user.getId());

		return ResponseEntity.status(200).body(response);
	}

	@Operation(summary = "ticket to email", description = "send the ticket to email", responses = {
			@ApiResponse(responseCode = "204", description = "email sent successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),

			@ApiResponse(responseCode = "400", description = "error with recipient", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "Error sending email", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "Unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))

	})
	// edit password
	@PostMapping("/html")
	public ResponseEntity<?> postHTMLEmail(@RequestBody Mail mail) {

//		this.emailService.sendHTMLEmail(mail, user.getId());
		this.emailService.sendHTMLEmail(mail);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Edit password", description = "edit user account password", responses = {
			@ApiResponse(responseCode = "204", description = "password edited successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),

			@ApiResponse(responseCode = "400", description = "Password cannot be null", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "invalid ticket", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "invalid ticket", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "the password is very short, minimum 6 characters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

			@ApiResponse(responseCode = "400", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

	})
	@PatchMapping("/password")
	public ResponseEntity<?> editPassword(@RequestBody @Valid EditPasswordDTO data) {

		this.authorizationService.EditPassword(data);
		return ResponseEntity.noContent().build();
	}

}
