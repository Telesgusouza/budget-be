package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.FriendRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;
	
	public void addNewFriend() {
		
	}

}
