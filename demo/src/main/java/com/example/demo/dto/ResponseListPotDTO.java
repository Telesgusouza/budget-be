package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Pot;

public record ResponseListPotDTO(
		List<Pot> content,
		Integer currentPage,
		Integer pageSize,
		Integer totalElements,
		Integer totalPages,
		boolean hasNext
		) {

}
