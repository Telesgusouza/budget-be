package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

import com.example.demo.entity.Budget;
import com.example.demo.entity.Friend;
import com.example.demo.entity.Pot;

public record ReponseAllInfoUser(
		UUID id,
		String login,
		String name,
		
		Double totalPots,
		
		List<PotDTO> pots,
		List<Friend> friends,
		List<Budget> budgets
		) {
	
}
