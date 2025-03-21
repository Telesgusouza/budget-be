package com.example.demo.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DeleteFriendDTO;
import com.example.demo.dto.EditFriendDTO;
import com.example.demo.dto.FriendDTO;
import com.example.demo.dto.ResponseListFriendDTO;
import com.example.demo.entity.Friend;
import com.example.demo.entity.User;
import com.example.demo.resource.exception.StandardError;
import com.example.demo.service.FriendService;
import com.example.demo.service.exception.AuthenticationFailed;
import com.example.demo.service.exception.ErrorLoggingIntoAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Tag(name = "Friend", description = "related operations manipulate friend")
@RestController
@RequestMapping(value = "/api/v1/friend")
public class FriendController {

	@Autowired
	private FriendService friendService;

	@Operation(
			summary = "add new friend",
			description = "add a new friend to your friends list",
			responses = {
					@ApiResponse(
							responseCode = "203",
							description = "success in adding new friend",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Friend.class))),
					
					@ApiResponse(
							responseCode = "409",
							description = "friend already exists in list",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class))),
					
			}
			)
	@PostMapping
	public ResponseEntity<Friend> addNewFriend(@AuthenticationPrincipal User user, @RequestBody FriendDTO data) {

		Friend friend = friendService.addNewFriend(user, data);

		return ResponseEntity.status(203).body(friend);
	}

	@Operation(
			summary = "get friends list",
			description = "should bring a list of friends, with pagination",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "success in bringing friends list",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ResponseListFriendDTO.class))),
					
					@ApiResponse(
							responseCode = "403",
							description = "invalid data",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = StandardError.class)))
					
			})
	@GetMapping
	public ResponseEntity<ResponseListFriendDTO> getListFriends(@AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(defaultValue = "10") @Max(15) @Min(1) int size) {

		if (user == null) {
			throw new AuthenticationFailed("invalid data");
		}

		// Criar objeto Pageable com paginação e ordenação por ID
		Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

		// Obter lista paginada de amigos
		List<Friend> friendsList = user.getFriends().stream().skip((long) page * size).limit(size)
				.collect(Collectors.toList());

		ResponseListFriendDTO response = new ResponseListFriendDTO(
				friendsList, 
				page, 
				size,
				user.getFriends().size(), 
				(int) Math.ceil((double) user.getFriends().size() / size), 
				page * size < user.getFriends().size()
				);
		
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(
			summary = "edit friend",
			description = "operation responsible for editing the friend",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "edited successfully",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Friend.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "very short name",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "new name cannot be the same as the previous one",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
			})
	@PatchMapping
	public ResponseEntity<Friend> editFriend(@RequestBody EditFriendDTO data) {
		
		Friend response = this.friendService.editFriend(data);
		
		return ResponseEntity.status(200).body(response);
	}

	@Operation(
			summary = "delete user",
			description = "operation responsible for deleting the friend",
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
							description = "friend not found",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "409",
							description = "Friend does not exist in list",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
					@ApiResponse(
							responseCode = "400",
							description = "an error occurred when deleting a friend",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = StandardError.class))),
					
			})
	@DeleteMapping
	public ResponseEntity<?> deleteFriend(@AuthenticationPrincipal User user, @RequestBody DeleteFriendDTO data) {

		friendService.deleteFriend(user, data);

		return ResponseEntity.noContent().build();
	}

}
