package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.DeleteFriendDTO;
import com.example.demo.dto.EditFriendDTO;
import com.example.demo.dto.FriendDTO;
import com.example.demo.entity.Friend;
import com.example.demo.entity.User;
import com.example.demo.repositories.FriendRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.InvalidField;
import com.example.demo.service.exception.ResourceAlreadyExists;
import com.example.demo.service.exception.ResourceNotFoundException;

@Service
public class FriendService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FriendRepository friendRepository;

	@Transactional
	public Friend addNewFriend(User user, FriendDTO data) {

		boolean existFriend = user.getFriends().stream().anyMatch(obj -> obj.getIdFriend().equals(data.id()));

		if (existFriend) {
			throw new ResourceAlreadyExists("friend already exists in list");
		}

		Friend newFriend = new Friend(null, data.img(), data.name(), data.id());

		newFriend.setUser(user);
		Friend saveFriend = friendRepository.save(newFriend);

		user.getFriends().add(saveFriend);

		return saveFriend;
	}

	@Transactional
	public Friend editFriend(EditFriendDTO data) {

		if (data.name().length() <= 0) {
			throw new InvalidField("very short name");
		}

		Optional<Friend> requestFriendOptional = this.friendRepository.findById(data.id());
		Friend requestFriend = requestFriendOptional
				.orElseThrow(() -> new ResourceNotFoundException("Friend not found"));

		if (data.name().equals(requestFriend.getName())) {
			throw new InvalidField("new name cannot be the same as the previous one");
		}

		requestFriend.setName(data.name());

		friendRepository.save(requestFriend);

		return requestFriend;

	}

	@Transactional
	public void deleteFriend(User user, DeleteFriendDTO data) {

		if (!friendRepository.existsById(data.id())) {
			throw new ResourceNotFoundException("friend not found");
		}

		Optional<Friend> requestFriend = this.friendRepository.findById(data.id());

		Friend friend = requestFriend.orElseThrow(() -> new ResourceNotFoundException("Friend not found"));

		boolean existFriend = user.getFriends().stream().anyMatch(obj -> obj.getId().equals(data.id()));

		if (!existFriend) {
			throw new ResourceAlreadyExists("Friend does not exist in list");
		}

		user.getFriends().removeIf(obj -> obj.getId().equals(friend.getId()));
		this.friendRepository.delete(friend);

		userRepository.save(user);

	}

}
