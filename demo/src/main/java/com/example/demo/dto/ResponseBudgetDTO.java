package com.example.demo.dto;

import java.util.UUID;

public record ResponseBudgetDTO(UUID id, String title, Float monthlyAmount) {

}
