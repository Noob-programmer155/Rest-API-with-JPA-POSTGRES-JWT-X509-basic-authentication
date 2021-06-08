package com.amrTm.restApiJpaJwtX509Authentication.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class TokenFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
	private TokenProvider provider;
	
	public TokenFilterConfig(TokenProvider provider) {
		super();
		this.provider = provider;
	}
	
	@Override
	public void configure(HttpSecurity builder) throws Exception {
		TokenFilter filter = new TokenFilter(provider);
		builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	}
}
