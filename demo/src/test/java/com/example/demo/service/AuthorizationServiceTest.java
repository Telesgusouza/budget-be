package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.config.TokenService;
import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.dto.EditPasswordDTO;
import com.example.demo.dto.EditUserDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.ResponseUserDTO;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.AuthenticationFailed;
import com.example.demo.service.exception.InvalidField;
import com.example.demo.service.exception.ResourceNotFoundException;
import com.example.demo.service.exception.TicketError;

@TestPropertySource(properties = { "cloud.aws.credentials.accessKey=your-access-key",
		"cloud.aws.credentials.secretKey=your-secret-key", "cloud.aws.region.static=us-east-1",
		"cloud.aws.stack.auto=false", "cloud.aws.kms.enabled=false" })
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private TicketService ticketService;

	@Mock
	private TokenService tokenService;

	@InjectMocks
	private AuthorizationService authorizationService;

	@Before(value = "")
	public void setup() {
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.getAndDelete(anyString())).thenReturn("user-id-123");

		when(ticketService.getUserByTicket(anyString())).thenReturn(Optional.of("user-id-123"));
	}

	@Test
	@DisplayName("Logged in successfully")
	public void loggedInSuccessfully() {
		String token = "my-token-123";
		AuthenticationDTO dataUser = new AuthenticationDTO("test@test.com", "111111");
		User user = new User(UUID.randomUUID(), "test@gmail.com", "111111", "test", UserRole.USER);

		var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		when(this.authenticationManager.authenticate(argThat(auth -> auth.getCredentials().equals(dataUser.password())
				&& auth.getPrincipal().equals(dataUser.login())))).thenReturn(authentication);

		when(tokenService.generateToken(user)).thenReturn(token);

		String result = authorizationService.login(dataUser);
		assertNotNull(result);
		assertEquals(token, result);
	}

	@Test
	@DisplayName("incorrect password")
	public void incorrectPassword() {
		String token = "my-token-123";
		AuthenticationDTO dataUser = new AuthenticationDTO("test_err@test.com", "111111");
		User user = new User(UUID.randomUUID(), "test@gmail.com", "111111", "test", UserRole.USER);

		var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		when(this.authenticationManager.authenticate(argThat(auth -> auth.getCredentials().equals(dataUser.password())
				&& auth.getPrincipal().equals(dataUser.login())))).thenThrow(BadCredentialsException.class);

		assertThrows(AuthenticationFailed.class, () -> authorizationService.login(dataUser));
	}

	@Test
	@DisplayName("Account deactivated or blocked")
	public void accountDeactivatedOrBlocked() {
		String token = "my-token-123";
		AuthenticationDTO dataUser = new AuthenticationDTO("test_err@test.com", "111111");
		User user = new User(UUID.randomUUID(), "test@gmail.com", "111111", "test", UserRole.USER);

		var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		when(this.authenticationManager.authenticate(argThat(auth -> auth.getCredentials().equals(dataUser.password())
				&& auth.getPrincipal().equals(dataUser.login())))).thenThrow(DisabledException.class);

		assertThrows(AuthenticationFailed.class, () -> authorizationService.login(dataUser));
	}

	// regster
	@Test
	@DisplayName("Registered successfully")
	public void registeredSuccessfully() {
		RegisterDTO data = new RegisterDTO("test@gmail.com", "111111", "test_name");
		User user = new User(UUID.randomUUID(), "test@gmail.com", "111111", "test_name", UserRole.USER);
		String token = "my_token_123";

		when(userRepository.findByLogin(data.login())).thenReturn(null);
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(tokenService.generateToken(user)).thenReturn(token);

		String result = this.authorizationService.register(data);
		assertNotNull(result);
		assertEquals(token, result);
	}

	@DisplayName("password very small")
	@Test
	public void passwordVerySmall() {

		RegisterDTO data = new RegisterDTO("test@gmail.com", "11111", "test_name");

		when(userRepository.findByLogin(data.login())).thenReturn(null);
		assertThrows(InvalidField.class, () -> authorizationService.register(data));

	}

	@DisplayName("invalid email")
	@Test
	public void invalidEmail() {

		RegisterDTO data = new RegisterDTO("format_incorret.gmail.com", "111111", "test_name");

		when(userRepository.findByLogin(data.login())).thenReturn(null);

		assertThrows(InvalidField.class, () -> authorizationService.register(data));

	}

	// delete
	@DisplayName("delete user")
	@Test
	public void deleteUser() {
		User user = new User(UUID.randomUUID(), "test@gmail.com", "password_123", "name_test", UserRole.USER);

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		doNothing().when(userRepository).delete(any(User.class));

		authorizationService.deleteAccount(user.getId());

		verify(userRepository).delete(user);
	}

	@DisplayName("user not found")
	@Test
	public void userNotFound() {
		User user = new User(UUID.randomUUID(), "test@gmail.com", "password_123", "name_test", UserRole.USER);

		when(userRepository.findById(user.getId())).thenThrow(ResourceNotFoundException.class);

		assertThrows(ResourceNotFoundException.class, () -> authorizationService.deleteAccount(user.getId()));
	}

	// edit user
	@DisplayName("User edited successfully")
	@Test
	public void userEditedSuccessfully() {
		User user = new User(UUID.randomUUID(), "teste@gmail.com", "password_123", "name_test", UserRole.USER);
		User newUser = new User(UUID.randomUUID(), "teste@gmail.com", "password_123", "teste_new_name",
				UserRole.USER);
		EditUserDTO dataUser = new EditUserDTO("teste_new_name");

		ResponseUserDTO data = new ResponseUserDTO(newUser.getId(), newUser.getUsername(),
				newUser.getName());

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(newUser);

		ResponseUserDTO response = authorizationService.editUser(dataUser, user.getId());

		assertNotNull(response);
	}

	@DisplayName("returns error with name same as previous")
	@Test
	public void returnsErrorWithNameSameAsPrevious() {
		User user = new User(UUID.randomUUID(), "teste@gmail.com", "password_123", "name_test", UserRole.USER);
		EditUserDTO dataUser = new EditUserDTO("name_test");

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		assertThrows(InvalidField.class, () -> authorizationService.editUser(dataUser, user.getId()));

	}

	@DisplayName("error because the name is not null")
	@Test
	public void errorBecausethenameisnotnull() {
		User user = new User(UUID.randomUUID(), "teste@gmail.com", "password_123", "name_test", UserRole.USER);
		EditUserDTO dataUser = new EditUserDTO(null);

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		assertThrows(InvalidField.class, () -> authorizationService.editUser(dataUser, user.getId()));
	}

	@DisplayName("password is not null")
	@Test
	public void passwordIsNotNull() {
		EditPasswordDTO data = new EditPasswordDTO(null, "ticket_123");

		assertThrows(InvalidField.class, () -> authorizationService.EditPassword(data));
	}

	@DisplayName("ticket is not null")
	@Test
	public void ticketIsNotNull() {
		EditPasswordDTO data = new EditPasswordDTO("password_123", null);

		assertThrows(InvalidField.class, () -> authorizationService.EditPassword(data));
	}

	@Test
	@DisplayName("should throw ticket error when user id optional is empty")
	public void shouldThrowTicketErrorWhenUserIdOptionalIsEmpty() {
		// Arrange (Preparação)
		String ticket = "ticket_invalido";
		String newPassword = "senha123456";

		EditPasswordDTO data = new EditPasswordDTO(newPassword, ticket);

		when(ticketService.getUserByTicket(ticket)).thenReturn(Optional.empty()); // Retorna Optional vazio

		assertThrows(TicketError.class, () -> {
			authorizationService.EditPassword(data);
		});

		verify(ticketService).getUserByTicket(ticket);

		verifyNoInteractions(userRepository);
		verifyNoMoreInteractions(redisTemplate);
	}

	@DisplayName("password is very small")
	@Test
	public void passwordIsVerySmall() {
		EditPasswordDTO data = new EditPasswordDTO("12345", "ticket_test");

		when(ticketService.getUserByTicket(data.ticket())).thenReturn(Optional.of("1235.8524.7416.9634"));

		assertThrows(InvalidField.class, () -> authorizationService.EditPassword(data));
	}

}
