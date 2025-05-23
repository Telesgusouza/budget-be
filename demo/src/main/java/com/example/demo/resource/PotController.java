package com.example.demo.resource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PotDTO;
import com.example.demo.dto.ResponseListPotDTO;
import com.example.demo.entity.Pot;
import com.example.demo.entity.User;
import com.example.demo.resource.exception.StandardError;
import com.example.demo.service.PotService;
import com.example.demo.service.exception.AuthenticationFailed;
import com.example.demo.service.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Tag(name = "Pot", description = "related operations manipulate pot")
@RestController
@RequestMapping(value = "/api/v1/pot")
public class PotController {

	@Autowired
	private PotService potService;

	@Operation(
			summary = "add new pot",
			description = "add a new pot to your pot list",
			responses = {
					
					@ApiResponse(
							responseCode = "201",
							description = "success in adding new Pot",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = Pot.class))),
					
					@ApiResponse(
							responseCode = "409",
							description = "pot already exists in list",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "403",
							description = "pot already exists in list",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),					
					
			})
	@PostMapping
	public ResponseEntity<Pot> addNewPot(@AuthenticationPrincipal User user, 
			@RequestBody PotDTO data) {

		if (user == null) {
			throw new AuthenticationFailed("invalid data");
		}

		Pot response = potService.newPot(user, data);

		return ResponseEntity.status(201).body(response);
	}

	@Operation(
			summary = "bring pot",
			description = "recover pot through id",
			responses = {
					
					@ApiResponse(
							responseCode = "200",
							description = "bringing pot through id",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = Pot.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "info pot not found",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "id cannot be null",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "pot not found",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
			})
	@GetMapping("/{id}")
	public ResponseEntity<Pot> getPot(@PathVariable UUID id) {

		Pot pot = potService.getIdPot(id);

		if (pot == null) {
			throw new ResourceNotFoundException("info pot not found");
		}

		return ResponseEntity.status(200).body(pot);
	}

	@Operation(
			summary = "bring list of pots",
			description = "Retrieves a list of traversable pots from the JWT token",
			responses = {
					
					@ApiResponse(
							responseCode = "200",
							description = "Pots successfully recovered",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = ResponseListPotDTO.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "page number must be greater than 0",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "403",
							description = "invalid data",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
			}
			)
	@GetMapping
	public ResponseEntity<ResponseListPotDTO> getAllPots(
			@AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Max(15) @Min(1) int size) {

		page -= 1;

		if (page == -1) {
			throw new ResourceNotFoundException("page number must be greater than 0");
		}

		if (user == null) {
			throw new AuthenticationFailed("invalid data");
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

		List<Pot> potList = user.getPots().stream().skip((long) page * size)
				.limit(size).collect(Collectors.toList());

		ResponseListPotDTO response = new ResponseListPotDTO(potList, page, size, user.getPots().size(),
				(int) Math.ceil((double) user.getPots().size() / size), 
				page * size < user.getPots().size());

		return ResponseEntity.status(200).body(response);
	}

	@Operation(
			summary = "edit pot",
			description = "operation responsible for editing a pot",
			responses = {
					
					@ApiResponse(
							responseCode = "200",
							description = "bringing pot through id",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = Pot.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "pot not found",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),					
					
			}
		)
	@PatchMapping("/{id}")
	public ResponseEntity<Pot> editPot(@RequestBody PotDTO data, @PathVariable UUID id) {

		Pot response = potService.editPot(data, id);

		return ResponseEntity.status(200).body(response);
	}

	@Operation(
			summary = "delete pot",
			description = "operation responsible for deleting a pot",
			responses = {
					
					@ApiResponse(
							responseCode = "204",
							description = "successfully deleted",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = Void.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "pot not found",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "An unexpected error occurred while deleting pot",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
			}
		)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePot(@PathVariable UUID id) {

		this.potService.deletePot(id);

		return ResponseEntity.noContent().build();
	}

}




















































