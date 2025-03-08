package com.example.demo.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.demo.entity.User;
import com.example.demo.service.exception.ErrorCreatingToken;
import com.example.demo.service.exception.ErrorToken;

@Service
public class TokenService {

	@Value("${api.security.token.secret}")
	private String secret;

	public String generateToken(User user) {

		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);

			String token = JWT.create().withIssuer("auth-api").withSubject(user.getUsername())
					.withExpiresAt(genExpirationDate()).sign(algorithm);

			return token;

		} catch (JWTCreationException exception) {

			throw new ErrorCreatingToken("Error while generating token. " + exception);
		}
	}

	public String validateToken(String token) {
		try {

			Algorithm algorithm = Algorithm.HMAC256(secret);

			return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();

		} catch (Exception e) {
			throw new ErrorToken("Error validating token");
		}
	}

	public Instant genExpirationDate() {

		return LocalDateTime.now().plusMonths(3).toInstant(ZoneOffset.of("-03:00"));
	}

}
