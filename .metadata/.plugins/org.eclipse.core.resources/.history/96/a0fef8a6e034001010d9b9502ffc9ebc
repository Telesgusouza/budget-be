package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.dto.BudgetDTO;
import com.example.demo.entity.Budget;
import com.example.demo.entity.UpdateDate;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.repositories.BudgetRepository;
import com.example.demo.repositories.UpdateDateRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.ResourceNotFoundException;

@TestPropertySource(properties = { "cloud.aws.credentials.accessKey=your-access-key",
		"cloud.aws.credentials.secretKey=your-secret-key", "cloud.aws.region.static=us-east-1",
		"cloud.aws.stack.auto=false", "cloud.aws.kms.enabled=false" })
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UpdateDateRepository updateDateRepository;

	@Mock
	private BudgetRepository budgetRepository;

	@InjectMocks
	private BudgetService budgetService;

	// addBudget
	@DisplayName("new budget added successfully")
	@Test
	public void newBudgetAddedSuccessfully() {
		User user = new User(UUID.randomUUID(), "img", "test@gmail.com", "password_123", "name test", UserRole.USER);
		user.setBudget(new ArrayList<>());
		BudgetDTO data = new BudgetDTO("title", "description", (float) 205.45);

		UpdateDate update = new UpdateDate(UUID.randomUUID(), Instant.now(), data.monthlyAmount());
		Budget budget = new Budget(null, "title", "description", (float) 205.45);

		budget.getUpdate().add(update);

		when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

		Budget request = budgetService.addBudget(user, data);

		assertNotNull(request);
	}

	@DisplayName("budget already exists in list")
	@Test
	public void budgetAlreadyExistsInList() {
		User user = new User(UUID.randomUUID(), "img", "test@gmail.com", "password_123", "name test", UserRole.USER);
		user.setBudget(new ArrayList<>());
		BudgetDTO data = new BudgetDTO("title", "description", (float) 205.45);

		UpdateDate update = new UpdateDate(UUID.randomUUID(), Instant.now(), data.monthlyAmount());
		Budget budget = new Budget(UUID.randomUUID(), "title", "description", (float) 205.45);

		user.getBudget().add(budget);

		assertThrows(ResourceNotFoundException.class, () -> budgetService.addBudget(user, data));

	}

	// getBudget
	@DisplayName("successfully recovered the budget")
	@Test
	public void successfullyRecoveredTheBudget() {
		Budget budget = new Budget(UUID.randomUUID(), "title", "description", (float) 205.45);
		when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));

		Budget response = budgetService.getBudget(budget.getId());

		assertNotNull(response);
	}

	@DisplayName("id is null")
	@Test
	public void idIsNull() {
		assertThrows(ResourceNotFoundException.class, () -> budgetService.getBudget(null));
	}

	@DisplayName("Editing the budget without changing the value")
	@Test
	public void editingTheBudgetWithoutChangingTheValue() {
		UUID budgetId = UUID.randomUUID(); // Criar uma única vez

		BudgetDTO data = new BudgetDTO("title edit", "description edit", (float) 205.45);
		Budget budget = new Budget(budgetId, "title", "description", (float) 205.45);
		Budget editBudget = new Budget(budgetId, data.title(), data.description(), data.monthlyAmount());

		when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
		when(budgetRepository.save(any(Budget.class))).thenReturn(editBudget);

		Budget request = budgetService.editBudget(data, budgetId);
		assertNotNull(request);
	}

	@DisplayName("Edit the budget and change the amount")
	@Test
	public void editTheBudgetAndChangeTheAmount() {
		UUID budgetId = UUID.randomUUID(); // Criar uma única vez

		BudgetDTO data = new BudgetDTO("title edit", "description edit", (float) 205.45);
		Budget budget = new Budget(budgetId, "title", "description", (float) 206.45);

		when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
		when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

		Budget request = budgetService.editBudget(data, budgetId);

		assertNotNull(request);
		assertNotNull(request.getUpdate().size() > 0);
		assertEquals(data.title(), request.getTitle());
		assertEquals(request.getUpdate().get(0).getValue(), data.monthlyAmount());
	}

	// delete budget
	@DisplayName("deleting the budget")
	@Test
	public void deletingTheBudget() {
		Budget budget = new Budget(UUID.randomUUID(), "title", "description", (float) 206.45);

		when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));
		doNothing().when(budgetRepository).delete(any(Budget.class));

		budgetService.deleteBudget(budget.getId());
		verify(budgetRepository).delete(budget);
	}

	@DisplayName("budget not found")
	@Test
	public void budgetNotFound() {
		when(budgetRepository.findById(UUID.randomUUID())).thenReturn(null);

		assertThrows(ResourceNotFoundException.class, () -> budgetService.deleteBudget(UUID.randomUUID()));
	}

}
