package com.amrTm.restApiJpaJwtX509Authentication.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

public class TokenFilter extends OncePerRequestFilter{
	private TokenProvider provider;
	
	public TokenFilter(TokenProvider provider) {
		super();
		this.provider = provider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = provider.resolveToken(request);
		try {
			if (token != null && provider.validateToken(token)) {
				Authentication auth = provider.getAuth(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}}
		catch(IllegalArgumentException e) {
			SecurityContextHolder.clearContext();
			throw new IllegalArgumentException(e.getMessage());
		}
		catch(JwtException e) {
			SecurityContextHolder.clearContext();
			throw new JwtException(e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}
}
