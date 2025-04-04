package com.example.demo.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PotDTO;
import com.example.demo.entity.Pot;
import com.example.demo.entity.UpdateDate;
import com.example.demo.entity.User;
import com.example.demo.repositories.PotRepository;
import com.example.demo.repositories.UpdateDateRepository;
import com.example.demo.repositories.UserRepository;

@Service
public class PotService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PotRepository potRepository;
	
	@Autowired
	private UpdateDateRepository updateDateRepository;
	
	@Transactional
	public Pot newPot(User user, PotDTO data) {
		
		UpdateDate update = new UpdateDate(null, Instant.now(), data.monthlyAmount());
		Pot pot = new Pot(null, data.title(), data.monthlyAmount());
		
		UpdateDate saveUpdate = updateDateRepository.save(update);
		
		Pot response = potRepository.save(pot);
		
		
		return response;
		
	}
	
}






























