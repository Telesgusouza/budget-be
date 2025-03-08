package com.example.demo.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.TransactionalService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transactional", description = "transaction operations between website accounts")
@RestController
@RequestMapping("/api/v1/transactional")
public class TransactionalController {

	@Autowired
	private TransactionalService transactionalService;

	@GetMapping
	public ResponseEntity<?> postMethodName() {

		return ResponseEntity.noContent().build();
	}

}
