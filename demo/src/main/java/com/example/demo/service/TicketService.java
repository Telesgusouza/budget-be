package com.example.demo.service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.ResourceNotFoundException;

@Service
public class TicketService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public String buildAndSaveTicket(String id) {

		System.out.println();
		
		System.out.println("===================================");
		System.out.println("CHEGOU AQUI");
		
		System.out.println();
		
		if (id == null) {
			throw new ResourceNotFoundException("the id was not passed");
		}

		String ticket = UUID.randomUUID().toString().substring(0, 6);

//		Optional<User> userOptional = this.userRepository.findById(id);
		String userId = this.userRepository.findByLogin(id).getId().toString();
		
		System.out.println();
		
		System.out.println("===================================");
		System.out.println(userId);
		
		System.out.println();
		
//		String userId = userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found")).getId()
//				.toString();

		redisTemplate.opsForValue().set(ticket, userId, Duration.ofMinutes(30));

		return ticket;
	}

	public Optional<String> getUserByTicket(String ticket) {
		return Optional.ofNullable(redisTemplate.opsForValue().getAndDelete(ticket));
	}

}
