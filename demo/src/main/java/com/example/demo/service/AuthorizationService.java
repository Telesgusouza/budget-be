package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
import com.example.demo.service.exception.ExceptionOfExistingEmail;
import com.example.demo.service.exception.InvalidField;
import com.example.demo.service.exception.ResourceNotFoundException;
import com.example.demo.service.exception.TicketError;

@Service
public class AuthorizationService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private TicketService ticketService;

	@Value("api.secret.key-transaction-password")
	private String secretKey;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userRepository.findByLogin(username);
	}

	public String login(AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

		try {
			var auth = this.authenticationManager.authenticate(usernamePassword);
			var token = tokenService.generateToken((User) auth.getPrincipal());

			return token;

		} catch (BadCredentialsException e) {

			throw new AuthenticationFailed("Incorrect password");
		} catch (AccountStatusException e) {

			throw new AuthenticationFailed("Account deactivated or blocked");
		}
	}

	public String register(RegisterDTO data) {

		if (this.userRepository.findByLogin(data.login()) != null) {

			throw new ExceptionOfExistingEmail("User already exists");
		}
		if (data.password().length() < 6) {

			throw new InvalidField("invalid password");
		}

		String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		if (!emailPattern.matcher(data.login()).find()) {

			throw new InvalidField("invalid email");
		}
		;

		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

		User newUser = new User(null, "", data.login(), encryptedPassword, data.name(), UserRole.USER);
		User user = this.userRepository.save(newUser);

		var token = tokenService.generateToken(user);

		return token;

	}

	public void deleteAccount(UUID id) {
		try {

			Optional<User> userOptional = this.userRepository.findById(id);
			User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found"));

			this.userRepository.delete(user);

		} catch (Exception e) {

			throw new ResourceNotFoundException("error when deleting user");
		}
	}

	public ResponseUserDTO editUser(EditUserDTO data, UUID id) {
		Optional<User> optionalUser = this.userRepository.findById(id);
		User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (user.getName().equals(data.name())) {
			throw new InvalidField("name cannot be the same as the previous name");
		}

		if (data.name() != null) {
			user.setName(data.name());
		} else {
			throw new InvalidField("name is not null");
		}

		User userEdit = this.userRepository.save(user);
		return new ResponseUserDTO(id, userEdit.getImg(), userEdit.getUsername(), userEdit.getName());

	}

	public void EditPassword(EditPasswordDTO data) {

		if (data.password() == null) {
			throw new InvalidField("Password cannot be null");
		}
		if (data.ticket() == null) {
			throw new InvalidField("Ticket cannot be null");
		}

		Optional<String> userIdOptional = ticketService.getUserByTicket(data.ticket());
		String userId = userIdOptional.orElseThrow(() -> new TicketError("invalid ticket"));

		if (userIdOptional.isEmpty()) {
			throw new TicketError("invalid ticket");
		}

		if (data.password().length() < 6) {
			throw new InvalidField("the password is very short, minimum 6 characters");
		}

		Optional<User> userOptional = this.userRepository.findById(UUID.fromString(userId));
		User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		String passwordEncript = new BCryptPasswordEncoder().encode(data.password());
		user.setPassword(passwordEncript);

		this.userRepository.save(user);

	}

}
