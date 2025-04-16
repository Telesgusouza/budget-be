package com.example.demo.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BudgetDTO;
import com.example.demo.entity.Budget;
import com.example.demo.entity.UpdateDate;
import com.example.demo.entity.User;
import com.example.demo.repositories.BudgetRepository;
import com.example.demo.repositories.PotRepository;
import com.example.demo.service.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class BudgetService {

	@Autowired
	private BudgetRepository budgetRepository;

	@Transactional
	public Budget addBudget(User user, BudgetDTO data) {
		
		boolean existBudget = user.getBudget().stream().anyMatch(obj -> obj.getTitle().equals(data.title()));

		if (existBudget) {
			throw new ResourceNotFoundException("budget already exists in list");
		}
		
		Budget budget = new Budget(null, data.title(), data.description(), data.monthlyAmount());
		UpdateDate update = new UpdateDate(null, Instant.now(), data.monthlyAmount());

		update.setBudget(budget);
		budget.getUpdate().add(update);
		budget.setUser(user);

		Budget response = budgetRepository.save(budget);

		user.getBudget().add(response);

		return response;
	}
	
	public Budget getBudget(UUID id) {
		
		if (id == null) {
			throw new ResourceNotFoundException("id cannot be null");
		}
		
		Optional<Budget> budgetOptional = budgetRepository.findById(id);
		Budget budget = budgetOptional.orElseThrow(() -> new ResourceNotFoundException("budget not found"));
		
		return budget;
	}

	@Transactional
	public Budget editBudget(BudgetDTO data, UUID id) {
		
		Optional<Budget> requestOptional = this.budgetRepository.findById(id);
		Budget request = requestOptional.orElseThrow(() -> new ResourceNotFoundException("budget not found"));
		
		if (!data.monthlyAmount().equals(request.getMonthlyAmount()) && data.monthlyAmount() != null) {
			
			UpdateDate update = new UpdateDate(null, Instant.now(), data.monthlyAmount());
			update.setBudget(request);
			request.getUpdate().add(update);
			
			request.setMonthlyAmount(data.monthlyAmount());
		}
		
		String title = data.title() == null && data.title().equals("") 
				? request.getTitle()
				: data.title();
		
		String description = data.description() == null || data.description().equals("")
				? request.getDescription()
				: data.description();
		
		request.setTitle(title);
		request.setDescription(description);
		
		Budget response = this.budgetRepository.save(request);
		
		return response;
	}
	
	@Transactional
	public void deleteBudget(UUID id) {
		
		try {
			
			Optional<Budget> requestOptional = budgetRepository.findById(id);
			Budget request = requestOptional.orElseThrow(() -> new ResourceNotFoundException("budget not found"));
			
			budgetRepository.delete(request);
			
		} catch (Exception e) {
			
			throw new ResourceNotFoundException("An unexpected error occurred while deleting pot");
		}
	}
	
}



















































