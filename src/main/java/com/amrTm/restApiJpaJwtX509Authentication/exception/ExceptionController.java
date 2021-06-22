package com.amrTm.restApiJpaJwtX509Authentication.exception;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler({AttributeNotFoundException.class, EntityNotFoundException.class, NullPointerException.class, NoSuchElementException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ExceptionContainer notFoundException(Exception e) {
		return new ExceptionContainer(HttpStatus.NOT_FOUND, LocalDateTime.now(), e.getMessage());
	}
	
	@ExceptionHandler(SaveAttributeException.class)
	@ResponseStatus(HttpStatus.NOT_MODIFIED)
	public ExceptionContainer modifyException(SaveAttributeException e) {
		return new ExceptionContainer(HttpStatus.NOT_MODIFIED, LocalDateTime.now(), e.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionContainer argumentProcessingFailed(IllegalArgumentException e) {
		return new ExceptionContainer(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
	}
	
	@ExceptionHandler(JwtException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ExceptionContainer notAcceptableToken(JwtException e) {
		return new ExceptionContainer(HttpStatus.FORBIDDEN, LocalDateTime.now(), e.getMessage());
	}
	
	@ExceptionHandler({AuthenticationException.class,AccessDeniedException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ExceptionContainer notAuthen(AuthenticationException e) {
		return new ExceptionContainer(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), "authority not allowed or not found");
	}
	
	@ExceptionHandler(MessagingException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionContainer message(MessagingException e) {
		return new ExceptionContainer(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "message can`t send");
	}
}
