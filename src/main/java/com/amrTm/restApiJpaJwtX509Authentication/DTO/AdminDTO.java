package com.amrTm.restApiJpaJwtX509Authentication.DTO;

import java.util.List;

import com.amrTm.restApiJpaJwtX509Authentication.entity.Role;

public class AdminDTO {
	private String username;
	private String password;
	private String email;
	private boolean validate;
	private List<Role> roles;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
