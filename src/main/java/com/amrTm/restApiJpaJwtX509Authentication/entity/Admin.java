package com.amrTm.restApiJpaJwtX509Authentication.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Admin {
	@Id
	@GeneratedValue
	private Long id;
	@NotBlank(message="username must be included")
	@Column(unique=true)
	private String username;
	@NotBlank(message="password must be included")
	@Size(min=8, message="password min 8 character")
	private String password;
	@NotBlank(message="email must be included")
	@Email(message="Must be a valid email", regexp="${email}")
	@Column(unique=true)
	private String email;
	private boolean validate;
	@ElementCollection
	private List<Role> role;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public List<Role> getRole() {
		return role;
	}
	public void setRole(List<Role> role) {
		this.role = role;
	}
}
