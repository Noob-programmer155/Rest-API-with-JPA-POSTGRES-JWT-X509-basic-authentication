package com.amrTm.restApiJpaJwtX509Authentication.services;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.entity.Role;
import com.amrTm.restApiJpaJwtX509Authentication.mailConfig.MailService;
import com.amrTm.restApiJpaJwtX509Authentication.repo.AdminRepo;
import com.amrTm.restApiJpaJwtX509Authentication.security.TokenProvider;

@Service
public class AdminService {
	private TokenProvider tokenProvider;
	private AuthenticationManager authenticationManager;
	private AdminRepo adminRepo;
	private MailService mailService;
	
	public AdminService(TokenProvider tokenProvider, AuthenticationManager authenticationManager, AdminRepo adminRepo, MailService mailService) {
		super();
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
		this.adminRepo = adminRepo;
		this.mailService = mailService;
	}

	public String signInAdmin(String username, String password) throws AuthenticationException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		return tokenProvider.createToken(username, adminRepo.findByUsername(username).getEmail(), adminRepo.findByUsername(username).getRole());
	}
	
	public String signUpAdmin(Admin admin) throws MessagingException {
		List<Role> jh = new ArrayList<>();
		jh.add(Role.ADMIN);
		jh.add(Role.USER);
		admin.setRole(jh);
		admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
		admin.setValidate(false);
		adminRepo.save(admin);
		String token = tokenProvider.createToken(admin.getUsername(),admin.getEmail(), admin.getRole());
		return mailService.sendValidationMessage(admin.getEmail(), admin.getUsername(), token);
	}
	
	public String refresh(String username) {
		return tokenProvider.createToken(username, adminRepo.findByUsername(username).getEmail(), adminRepo.findByUsername(username).getRole());
	}
	
	public boolean validateLink(String token, HttpServletResponse res) {
		res.setHeader("Authorization", token);
		Admin admin = adminRepo.findByUsername(tokenProvider.getAuth(token).getName());
		admin.setValidate(true);
		adminRepo.save(admin);
		return true;
	}
}
