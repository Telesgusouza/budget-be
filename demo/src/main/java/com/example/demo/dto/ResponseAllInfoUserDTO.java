package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

import com.example.demo.entity.Account;
import com.example.demo.entity.Budget;
import com.example.demo.entity.Pot;
import com.example.demo.entity.Transaction;

public record ResponseAllInfoUserDTO(UUID id, String img, String login, String name,

		List<Transaction> transaction, List<Pot> pots, Account account, Budget budget) {

}
