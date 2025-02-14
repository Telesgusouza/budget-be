package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.example.demo.config.TokenService;
import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.AuthenticationFailed;
import com.example.demo.service.exception.InvalidField;

@SpringBootTest(properties = { "api.security.token.secret=${JWT_SECRET:my-secret-key}", })
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private TokenService tokenService;

	@InjectMocks
	private AuthorizationService authorizationService;

	// login
	@Test
	@DisplayName("Logged in successfully")
	public void loggedInSuccessfully() {
		String token = "my-token-123";
		AuthenticationDTO dataUser = new AuthenticationDTO("test@test.com", "111111");
		User user = new User(UUID.randomUUID(), "", "test@gmail.com", "111111", "test", UserRole.USER);

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
		User user = new User(UUID.randomUUID(), "", "test@gmail.com", "111111", "test", UserRole.USER);

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
		User user = new User(UUID.randomUUID(), "", "test@gmail.com", "111111", "test", UserRole.USER);

		var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		// Configurar o authenticationManager para lançar DisabledException
		when(this.authenticationManager.authenticate(argThat(auth -> auth.getCredentials().equals(dataUser.password())
				&& auth.getPrincipal().equals(dataUser.login())))).thenThrow(DisabledException.class);

		assertThrows(AuthenticationFailed.class, () -> authorizationService.login(dataUser));
	}

	// regster
	@Test
	@DisplayName("Registered successfully")
	public void registeredSuccessfully() {
		RegisterDTO data = new RegisterDTO("test@gmail.com", "111111", "test_name");
		User user = new User(UUID.randomUUID(), "", "test@gmail.com", "111111", "test_name", UserRole.USER);
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

}




























