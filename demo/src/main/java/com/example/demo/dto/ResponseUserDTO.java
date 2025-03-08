package com.example.demo.dto;

import java.util.UUID;

public record ResponseUserDTO(
		UUID id,
		String img,
		String login,
		String name) {

}
