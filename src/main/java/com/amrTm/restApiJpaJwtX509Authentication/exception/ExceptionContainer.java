package com.amrTm.restApiJpaJwtX509Authentication.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class ExceptionContainer {
	private HttpStatus httpStatus; 
	@JsonFormat(shape = Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	public ExceptionContainer(HttpStatus httpStatus, LocalDateTime timestamp, String message) {
		super();
		this.httpStatus = httpStatus;
		this.timestamp = timestamp;
		this.message = message;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
