package com.amrTm.restApiJpaJwtX509Authentication.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {
	@Value("${Authentication.Jwt.secret-key}")
	private String secretKey;
	
	@Value("${Authentication.Jwt.validity}")
	private long validity;
	
	@Autowired
	private UserService user;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(String username) {
		UserDetails admin = getUserDetails(username);
		Claims claim = Jwts.claims().setSubject(admin.getUsername());
		claim.put(admin.getPassword(), admin.getAuthorities());
		Date now = new Date();
		Date expired = new Date(now.getTime()+validity);
		return Jwts.builder()
				.setClaims(claim)
				.setIssuedAt(now)
				.setExpiration(expired)
				.signWith(SignatureAlgorithm.RS512, secretKey)
				.compact();
	}
	
	public Authentication getAuth(String token) {
		String name = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		UserDetails detail = user.loadUserByUsername(name);
		return new UsernamePasswordAuthenticationToken(detail,"",detail.getAuthorities());
	}
	
	public String resolveToken(HttpServletRequest req) {
		String auth = req.getHeader("Authorization");
		if(auth != null && auth.startsWith("Bearer "))
			return auth.substring(7);
		return null;
	}
	
	public boolean validateToken(String token) throws JwtException, IllegalArgumentException{
		Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token);
		return true;
	}
	
	private UserDetails getUserDetails(String name) {
		return user.loadUserByUsername(name);
	}
}
