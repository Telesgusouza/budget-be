package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.dto.DeleteFriendDTO;
import com.example.demo.dto.EditFriendDTO;
import com.example.demo.dto.FriendDTO;
import com.example.demo.entity.Friend;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.repositories.FriendRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.InvalidField;
import com.example.demo.service.exception.ResourceAlreadyExists;
import com.example.demo.service.exception.ResourceNotFoundException;

@TestPropertySource(properties = { "cloud.aws.credentials.accessKey=your-access-key",
		"cloud.aws.credentials.secretKey=your-secret-key", "cloud.aws.region.static=us-east-1",
		"cloud.aws.stack.auto=false", "cloud.aws.kms.enabled=false" })
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private FriendRepository friendRepository;

	@InjectMocks
	private FriendService friendService;

	@Test
	@DisplayName("added friend successfully")
	public void addedFriendSuccessfully() {

		User user = new User(UUID.randomUUID(), "test@gmail.com", "password_123", "name test", UserRole.USER);

		Friend friend = new Friend(UUID.randomUUID(), "img", "name test", UUID.randomUUID(), user);

		user.getFriends().add(friend);

		FriendDTO data = new FriendDTO("img", "name test", UUID.randomUUID());

		when(friendRepository.save(any(Friend.class))).thenReturn(friend);

		Friend request = friendService.addNewFriend(user, data);

		assertNotNull(request);
	}

	@Test
	@DisplayName("friend already exists")
	public void friendAlreadyExists() {
		User user = new User(UUID.randomUUID(), "test@gmail.com", "password_123", "name test", UserRole.USER);

		// Garante que a lista existe antes de adicionar amigos
		user.setFriends(new ArrayList<>());

		Friend friend = new Friend(UUID.randomUUID(), "img", "name test", UUID.randomUUID(), user);

		user.getFriends().add(friend);

		FriendDTO data = new FriendDTO(friend.getImg(), friend.getName(), friend.getIdFriend());

		assertThrows(ResourceAlreadyExists.class, () -> friendService.addNewFriend(user, data));
	}

	@Test
	@DisplayName("edited successfully")
	public void editedSuccessfully() {
		EditFriendDTO data = new EditFriendDTO("new name", UUID.randomUUID());
		Friend friend = new Friend(UUID.randomUUID(), "img", "original name", UUID.randomUUID());
		Friend editFriend = new Friend(friend.getId(), friend.getImg(), data.name(), friend.getIdFriend());

		Friend originalFriend = new Friend(UUID.randomUUID(), "img", "original name", UUID.randomUUID());

		when(friendRepository.findById(any(UUID.class))).thenReturn(Optional.of(friend));
		when(friendRepository.save(any(Friend.class))).thenReturn(editFriend);

		Friend request = friendService.editFriend(data);
		assertNotEquals(originalFriend.getName(), request.getName());
		assertEquals(editFriend.getName(), request.getName());
	}

	@Test
	@DisplayName("very short name")
	public void veryShortName() {

		EditFriendDTO data = new EditFriendDTO("", UUID.randomUUID());

		assertThrows(InvalidField.class, () -> friendService.editFriend(data));
	}

	@Test
	@DisplayName("new name cannot be the same as the previous one")
	public void newNameCannotBeTheSameAsThePreviousOne() {
		EditFriendDTO data = new EditFriendDTO("original name", UUID.randomUUID());
		Friend friend = new Friend(UUID.randomUUID(), "img", "original name", UUID.randomUUID());
		Friend editFriend = new Friend(friend.getId(), friend.getImg(), data.name(), friend.getIdFriend());

		Friend originalFriend = new Friend(UUID.randomUUID(), "img", "original name", UUID.randomUUID());

		when(friendRepository.findById(any(UUID.class))).thenReturn(Optional.of(friend));

		assertThrows(InvalidField.class, () -> friendService.editFriend(data));

	}

	@Test
	@DisplayName("friend deleted successfully")
	public void friendDeletedSuccessfully() {
		UUID friendId = UUID.randomUUID();
		User user = new User(UUID.randomUUID(), "test@gmail.com", "password_123", "name test", UserRole.USER);

		Friend friend = new Friend(friendId, "img", "original name", UUID.randomUUID());
		DeleteFriendDTO data = new DeleteFriendDTO(friendId);

		user.getFriends().add(friend);

		when(friendRepository.existsById(data.id())).thenReturn(true);
		when(friendRepository.findById(data.id())).thenReturn(Optional.of(friend));

		friendService.deleteFriend(user, data);
		verify(friendRepository).delete(friend);
	}

	@Test
	@DisplayName("friend not found")
	public void friendNotFound() {
		User user = new User(UUID.randomUUID(), "test@gmail.com", "password_123", "name test", UserRole.USER);

		DeleteFriendDTO data = new DeleteFriendDTO(UUID.randomUUID());

		when(friendRepository.existsById(any(UUID.class))).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> friendService.deleteFriend(user, data));
	}

	@Test
	@DisplayName("friend does not exist in friends list")
	public void friendDoesNotExistInFriendsList() {

		UUID friendId = UUID.randomUUID();
		User user = new User(UUID.randomUUID(), "test@gmail.com", "password_123", "name test", UserRole.USER);

		Friend friend = new Friend(friendId, "img", "original name", UUID.randomUUID());
		DeleteFriendDTO data = new DeleteFriendDTO(friendId);

		when(friendRepository.existsById(data.id())).thenReturn(true);
		when(friendRepository.findById(data.id())).thenReturn(Optional.of(friend));

		assertThrows(ResourceAlreadyExists.class, () -> friendService.deleteFriend(user, data));

	}

}
