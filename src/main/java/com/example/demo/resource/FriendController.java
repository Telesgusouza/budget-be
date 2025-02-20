package com.example.demo.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.FriendService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Friend", description = "Contains operations related to adding, listing and editing friends in friends list")
@RestController
@RequestMapping("/api/v1/friend")
public class FriendController {

	@Autowired
	private FriendService friendService;
	
}
