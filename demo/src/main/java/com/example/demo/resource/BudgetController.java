package com.example.demo.resource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BudgetDTO;
import com.example.demo.dto.ResponseBudgetDTO;
import com.example.demo.dto.ResponseListBudgetDTO;
import com.example.demo.entity.Budget;
import com.example.demo.entity.User;
import com.example.demo.service.BudgetService;
import com.example.demo.service.exception.AuthenticationFailed;
import com.example.demo.service.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/v1/budget")
public class BudgetController {

	@Autowired
	private BudgetService budgetService;

	@PostMapping
	public ResponseEntity<Budget> addNewBudget(@AuthenticationPrincipal User user, @RequestBody BudgetDTO data) {

		if (user == null) {
			throw new AuthenticationFailed("invalid data");
		}

		Budget response = budgetService.addBudget(user, data);

		return ResponseEntity.status(201).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Budget> getBudget(@PathVariable UUID id) {

		Budget budget = budgetService.getBudget(id);

		if (budget == null) {
			throw new ResourceNotFoundException("info budget not found");
		}

		return ResponseEntity.status(200).body(budget);
	}

	@GetMapping
	public ResponseEntity<ResponseListBudgetDTO> getAllBudgets(@AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Max(15) @Min(1) int size) {

		page -= 1;

		if (page == -1) {
			throw new ResourceNotFoundException("page number must be greater than 0");
		}

		if (user == null) {
			throw new AuthenticationFailed("Invalid data");
		}

		List<Budget> budgetList = user.getBudget().stream().skip((long) page * size).limit(size)
				.collect(Collectors.toList());

		List<ResponseBudgetDTO> budgets = budgetList.stream()
				.map(budget -> new ResponseBudgetDTO(budget.getId(), budget.getTitle(), budget.getMonthlyAmount()))
				.collect(Collectors.toList());

		ResponseListBudgetDTO response = new ResponseListBudgetDTO(budgets, page, size, user.getBudget().size(),
				(int) Math.ceil((double) user.getBudget().size() / size), page * size < user.getBudget().size());

		return ResponseEntity.status(200).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Budget> editBudget(@Valid @RequestBody BudgetDTO data, @PathVariable UUID id) {

		Budget budget = budgetService.editBudget(data, id);

		return ResponseEntity.status(200).body(budget);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBudget(@PathVariable UUID id) {

		budgetService.deleteBudget(id);

		return ResponseEntity.noContent().build();
	}

}
