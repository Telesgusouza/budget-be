package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Budget;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"updates"})
public record ResponseListBudgetDTO(
			List<ResponseBudgetDTO> content,
			Integer currentPage,
			Integer pageSize,
			Integer totalElements,
			Integer totalPages,
			boolean hasNext
		) {

}
