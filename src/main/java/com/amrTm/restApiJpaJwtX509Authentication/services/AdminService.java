package com.amrTm.restApiJpaJwtX509Authentication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.security.TokenProvider;

@Service
public class AdminService {
	private TokenProvider tokenProvider;
	private AuthenticationManager authenticationManager;
	
	public AdminService(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
		super();
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	public String signupAdmin(String username, String password) throws AuthenticationException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		return tokenProvider.createToken(username);
	}
	
	public String refresh(String username) {
		return tokenProvider.createToken(username);
	}
	
	
	
	// if using jdbc or other database, something like this:
	
//	public String signinAdmin(Admin admin) {
//		adminRepo.save(admin);
//		return tokenProvider(admin.getName());
//	}
}
