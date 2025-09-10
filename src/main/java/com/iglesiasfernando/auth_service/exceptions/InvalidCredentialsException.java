package com.iglesiasfernando.auth_service.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException(String message) {
		super(message);
	}

	public EmailAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
