package com.example.demo.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FriendDTO;
import com.example.demo.entity.User;
import com.example.demo.service.FriendService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Friend", description = "related operations manipulate friend")
@RestController
@RequestMapping(value = "/api/v1/friend")
public class FriendController {

	@Autowired
	private FriendService friendService;

	@PostMapping
	public ResponseEntity<?> addNewFriend(@AuthenticationPrincipal User user, @RequestBody FriendDTO data) {

//		Friend friend = friendService.addNewFriend(user, data);

//		return ResponseEntity.status(203).body(friend);
		return null;
	}

}
