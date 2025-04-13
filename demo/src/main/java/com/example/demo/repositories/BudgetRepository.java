package com.example.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

}
