package com.amrTm.restApiJpaJwtX509Authentication.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Role;
import com.amrTm.restApiJpaJwtX509Authentication.repository.AdminRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
	
	@Autowired
	private AdminRepo adminRepo;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String setToken(String username, String email, List<Role> roles) {
		Claims claim = Jwts.claims().setSubject(username);
		claim.put("email", email);
		claim.put("Auth", roles);
		Date now = new Date();
		Date expired = new Date(now.getTime()+validity);
		return Jwts.builder()
				.setClaims(claim)
				.setIssuedAt(now)
				.setExpiration(expired)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
	
	public boolean createToken(String username, String email, List<Role> roles, HttpServletResponse res, HttpServletRequest req) {
		Cookie kie = WebUtils.getCookie(req, "JWT_BEARER");
		if(kie==null) {
			String token = setToken(username, email, roles);
			Cookie kei = new Cookie("JWT_BEARER", token);
			kei.setHttpOnly(true);
			kei.setPath("/");
			kei.setSecure(true);
			res.addCookie(kei);
			return true;
		}
		else {
			kie.setMaxAge(0);
			res.addCookie(kie);
			String token = setToken(username, email, roles);
			Cookie kei = new Cookie("JWT_BEARER", token);
			kei.setHttpOnly(true);
			kei.setPath("/");
			kei.setSecure(true);
			res.addCookie(kei);
			return true;
		}
	}
	
	public Authentication getAuth(String token) {
		String name = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		UserDetails detail = user.loadUserByUsername(name);
		return new UsernamePasswordAuthenticationToken(detail.getUsername(),detail.getPassword(),detail.getAuthorities());
	}
	
	public String resolveToken(HttpServletRequest req) {
//		if using header for authentication
		
//		String auth = req.getHeader("Authorization");
//		if(auth != null && auth.startsWith("Bearer "))
//			return auth.substring(7);
//		return null;
		
		Cookie kie = WebUtils.getCookie(req, "JWT_BEARER");
		if(kie != null) {
			return kie.getValue();
		}
		return null;
	}
	
	public boolean validateToken(String token) {
		try {
		Jws<Claims> hg = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		if(adminRepo.existsByUsernameAndEmail(hg.getBody().getSubject(),(String)hg.getBody().get("email")))
			return true;
		return false;
		}
		catch(JwtException | IllegalArgumentException e) {
			throw new JwtException("expired or invalid JWT token");
		}
	}
	
	public boolean validateToken(HttpServletRequest req) {
		try {
		Cookie e = WebUtils.getCookie(req, "JWT_BEARER");
		return validateToken(e.getValue());}
		catch(NullPointerException e) {
			throw new JwtException("expired or invalid JWT token");
		}
	}
}
