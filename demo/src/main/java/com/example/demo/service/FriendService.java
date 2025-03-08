package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class FriendService {
//
//	@Autowired
//	private FriendRepository friendRepository;
//
//	@Autowired
//	private UserRepository userRepository;
//	
//	@Transactional
//	public Friend addNewFriend(User user, FriendDTO data) {
//	    boolean existFriend = user.getFriends().stream().anyMatch(obj -> obj.getIdFriend().equals(data.id()));
//	    if (existFriend) {
//	        throw new ResourceAlreadyExists("Amigo já existe na lista");
//	    }
//	    
//	    // Primeiro, salve o usuário para garantir que o ID está definido
//	    User savedUser = userRepository.save(user);
//	    
//	    Friend newFriend = new Friend(null, data.img(), data.name(), data.id(), savedUser);
//	    Friend saveFriend = this.friendRepository.save(newFriend);
//	    
//	    // Atualize a coleção de friends do usuário
//	    savedUser.getFriends().add(saveFriend);
//	    userRepository.save(savedUser);
//	    
//	    return saveFriend;
//	}

//	@Transactional
//	public Friend addNewFriend(User user, FriendDTO data) {
//		boolean existFriend = user.getFriends().stream().anyMatch(obj -> obj.getIdFriend().equals(data.id()));
//
//		if (existFriend) {
//			throw new ResourceAlreadyExists("Amigo já existe na lista");
//		}
//
//		Friend newFriend = new Friend(null, data.img(), data.name(), data.id(), user);
//
//		System.out.println();
//
//		System.out.println("===========================");
//		System.out.println(newFriend);
//		System.out.println(newFriend.getUser());
//
//		Friend saveFriend = this.friendRepository.save(newFriend);
//
//		user.getFriends().add(saveFriend);
//
//		userRepository.save(user);
//
//		return saveFriend;
//	}

}
