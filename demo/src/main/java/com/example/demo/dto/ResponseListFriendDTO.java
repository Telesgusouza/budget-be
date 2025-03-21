package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Friend;

public record ResponseListFriendDTO(
		List<Friend> content,
		Integer currentPage,
		Integer pageSize,
		Integer totalElements,
		Integer totalPages,
		boolean hasNext
		) {

}
