package com.amrTm.restApiJpaJwtX509Authentication.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{
	@Value("${Authentication.user}")
	private String userAdmin;
	
	@Value("${Authentication.password}")
	private String passAdmin;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		if (!username.equals(userAdmin)) {
			throw new UsernameNotFoundException("Username not found");
		}
		return User.withUsername(userAdmin)
				.password(new BCryptPasswordEncoder().encode(passAdmin))
				.roles("ADMIN","USER")
				.build();
	}
}
