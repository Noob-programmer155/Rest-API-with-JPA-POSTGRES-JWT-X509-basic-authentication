package com.amrTm.restApiJpaJwtX509Authentication.exception;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler({AttributeNotFoundException.class, EntityNotFoundException.class})
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
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public ExceptionContainer notAcceptableToken(JwtException e) {
		return new ExceptionContainer(HttpStatus.NOT_ACCEPTABLE, LocalDateTime.now(), e.getMessage());
	}
}
