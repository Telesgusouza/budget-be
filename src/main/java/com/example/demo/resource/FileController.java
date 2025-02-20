package com.example.demo.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ResponseUrlPhotoDTO;
import com.example.demo.dto.ResultResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.service.S3Service;
import com.example.demo.service.exception.InvalidField;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User photo", description = "operations responsible for manipulating the user's photo")
@RestController
@RequestMapping("/api/v1/file")
public class FileController {

	@Autowired
	private S3Service s3Service;

	@Operation(
			summary = "add new photo", 
			description = "add photo to change user photo",
			responses = {
					
					@ApiResponse(
							responseCode = "200",
							description = "photo added successfully", 
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = ResultResponseDTO.class))),
					
					@ApiResponse(
							responseCode = "200",
							description = "photo added successfully", 
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = ResultResponseDTO.class))),
					
			})
	@PostMapping
	public ResultResponseDTO upload(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {

		if (user == null) {
			throw new InvalidField("Invalid token or unauthenticated user");
		}

		return s3Service.upload(file, user.getId(), user);
	}

	@DeleteMapping
	public ResultResponseDTO deleteFile(@AuthenticationPrincipal User user) {

		if (user == null) {
			throw new InvalidField("Invalid token or unauthenticated user");
		}

		return s3Service.delete(user.getId(), user);
	}

	@GetMapping
	public ResponseUrlPhotoDTO getPhoto(@AuthenticationPrincipal User user) {

		if (user == null) {
			throw new InvalidField("Invalid token or unauthenticated user");
		}

		return s3Service.getPhoto(user.getId());

	}

}
