package com.amrTm.restApiJpaJwtX509Authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Admin;
import com.amrTm.restApiJpaJwtX509Authentication.repository.AdminRepo;

@Service
public class UserService implements UserDetailsService{
//	@Value("${Authentication.user}")
//	private String userAdmin;
//	
//	@Value("${Authentication.password}")
//	private String passAdmin;
	
	@Autowired
	private AdminRepo adminRepo;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Admin admin = adminRepo.findByUsername(username);
		if(!admin.isValidation()) {
			return User.withUsername(admin.getUsername())
					.password(admin.getPassword())
					.authorities(admin.getRole())
					.accountExpired(false)
					.accountLocked(true)
					.credentialsExpired(false)
					.build();
		}
		else {
			return User.withUsername(admin.getUsername())
					.password(admin.getPassword())
					.authorities(admin.getRole())
					.accountExpired(false)
					.accountLocked(false)
					.credentialsExpired(false)
					.build();
		}
	}
}
