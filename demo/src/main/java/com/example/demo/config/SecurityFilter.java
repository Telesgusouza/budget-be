package com.example.demo.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. Obter o contexto de segurança
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			logger.debug("Null security context, allowing access");
			filterChain.doFilter(request, response);
			return;
		}

		// 2. Obter a autenticação
		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			logger.debug("Null authentication, allowing access");
			filterChain.doFilter(request, response);
			return;
		}

		// 3. Verificar se é uma instância de UserDetails
		if (!(authentication.getPrincipal() instanceof UserDetails)) {
			logger.debug("Principal is not an instance of UserDetails");
			filterChain.doFilter(request, response);
			return;
		}

		// 4. Agora é seguro acessar as autoridades
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

		// Continuar com o resto da lógica do filtro
		filterChain.doFilter(request, response);
	}

	private String recoverToken(HttpServletRequest request) {

		var authHeader = request.getHeader("Authorization");

		if (authHeader == null) {
			return null;
		}

		return authHeader.replace("Bearer ", "");

	}

}
