package com.example.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	User findByLogin(String login);
	boolean existsByLogin(String login);
	
}

