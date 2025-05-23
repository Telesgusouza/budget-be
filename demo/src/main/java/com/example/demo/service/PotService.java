package com.example.demo.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PotDTO;
import com.example.demo.entity.Pot;
import com.example.demo.entity.UpdateDate;
import com.example.demo.entity.User;
import com.example.demo.repositories.PotRepository;
import com.example.demo.service.exception.ResourceAlreadyExists;
import com.example.demo.service.exception.ResourceNotFoundException;

@Service
public class PotService {

	@Autowired
	private PotRepository potRepository;

	@Transactional
	public Pot newPot(User user, PotDTO data) {

		boolean existPot = user.getPots().stream().anyMatch(obj -> obj.getTitle().equals(data.title()));

		if (existPot) {
			throw new ResourceAlreadyExists("pot already exists in list");
		}

		Pot pot = new Pot(null, data.title(), data.description(), data.monthlyAmount(), new ArrayList<>());

		UpdateDate update = new UpdateDate(null, Instant.now(), data.monthlyAmount());
		update.setPot(pot);
		pot.getUpdate().add(update);

		pot.setUser(user);

		Pot response = potRepository.save(pot);
		user.getPots().add(response);

		return response;
	}

	public Pot getIdPot(UUID id) {

		if (id == null) {
			throw new ResourceNotFoundException("id cannot be null");
		}

		Optional<Pot> potOptional = potRepository.findById(id);
		Pot pot = potOptional.orElseThrow(() -> new ResourceNotFoundException("pot not found"));

		return pot;
	}

	public Pot editPot(PotDTO data, UUID id) {

		Optional<Pot> requestOptional = this.potRepository.findById(id);
		Pot request = requestOptional.orElseThrow(() -> new ResourceNotFoundException("pot not found"));

		if (!data.monthlyAmount().equals(request.getMonthlyAmount()) && data.monthlyAmount() != null) {

			UpdateDate update = new UpdateDate(null, Instant.now(), data.monthlyAmount());
			update.setPot(request);
			request.getUpdate().add(update);

			request.setMonthlyAmount(data.monthlyAmount());
		}

		String title = data.title() == null && data.title().equals("") 
				? request.getTitle() 
				: data.title();

		String description = data.description() == null || data.description().equals("") 
				? request.getTitle()
				: data.title();

		request.setTitle(title);
		request.setDescription(description);

		Pot response = this.potRepository.save(request);

		return response;
	}

	public void deletePot(UUID id) {

		try {

			Optional<Pot> requestOptional = potRepository.findById(id);
			Pot request = requestOptional.orElseThrow(() -> new ResourceNotFoundException("pot not found"));

			potRepository.delete(request);

		} catch (Exception e) {

			throw new ResourceNotFoundException("An unexpected error occurred while deleting pot");
		}

	}

}
























































