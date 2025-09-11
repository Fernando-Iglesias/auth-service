package com.iglesiasfernando.auth_service.exceptions;

import com.iglesiasfernando.auth_service.dtos.ErrorDTO;
import com.iglesiasfernando.auth_service.dtos.ErrorResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private enum ERROR_CODES {
		EMAIL_IS_MANDATORY(1),
		INVALID_EMAIL(2),
		PASSWORD_IS_MANDATORY(3),
		INVALID_PASSWORD_LENGTH(4),
		INVALID_PASSWORD_PATTERN(5),
		USER_ALREADY_EXISTS(6),
		DATA_INTEGRITY_VIOLATION(7),
		INVALID_CREDENTIALS(8);

		private final int code;

		ERROR_CODES(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Integer> validationErrorCodes = Map.of(
			"Email is mandatory", ERROR_CODES.EMAIL_IS_MANDATORY.getCode(),
			"Invalid email format", ERROR_CODES.INVALID_EMAIL.getCode(),
			"Password is mandatory", ERROR_CODES.PASSWORD_IS_MANDATORY.getCode(),
			"Password must be between 8 and 12 characters", ERROR_CODES.INVALID_PASSWORD_LENGTH.getCode(),
			"Password must contain lowercase letters, exactly one uppercase letter and exactly two digits", ERROR_CODES.INVALID_PASSWORD_PATTERN.getCode()
		);

		List<ErrorDTO> errors = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> new ErrorDTO(
				LocalDateTime.now(),
				validationErrorCodes.getOrDefault(error.getDefaultMessage(), 9999),
				error.getDefaultMessage()
			))
			.collect(Collectors.toList());

		ErrorResponseDTO response = new ErrorResponseDTO(errors);
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDTO> handleUserExists(EmailAlreadyExistsException ex) {
		ErrorDTO error = new ErrorDTO(
			LocalDateTime.now(),
			ERROR_CODES.USER_ALREADY_EXISTS.getCode(),
			ex.getMessage()
		);

		ErrorResponseDTO response = new ErrorResponseDTO(List.of(error));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		ErrorDTO error = new ErrorDTO(
			LocalDateTime.now(),
			ERROR_CODES.DATA_INTEGRITY_VIOLATION.getCode(),
			"Data integrity violation: " + ex.getMessage()
		);

		ErrorResponseDTO response = new ErrorResponseDTO(List.of(error));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(InvalidCredentialsException ex) {
		ErrorDTO error = new ErrorDTO(
			LocalDateTime.now(),
			ERROR_CODES.INVALID_CREDENTIALS.getCode(),
			ex.getMessage()
		);

		ErrorResponseDTO response = new ErrorResponseDTO(List.of(error));
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
}

