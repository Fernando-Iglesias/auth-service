package com.iglesiasfernando.auth_service.exceptions;

public class InvalidCredentialsException extends RuntimeException {
	public InvalidCredentialsException(String message) {
		super(message);
	}

}
