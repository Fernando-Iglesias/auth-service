package com.iglesiasfernando.auth_service.controllers;

import com.iglesiasfernando.auth_service.converters.PhoneConverter;
import com.iglesiasfernando.auth_service.converters.UserConverter;
import com.iglesiasfernando.auth_service.dtos.LoginResponseDTO;
import com.iglesiasfernando.auth_service.dtos.SignUpRequestDTO;
import com.iglesiasfernando.auth_service.dtos.SignUpResponseDTO;
import com.iglesiasfernando.auth_service.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping
	@RequestMapping("/signup")
	public ResponseEntity<SignUpResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO dto) {
		AuthService.LoggedUser loggedUser = authService.signUp(
			dto.getEmail(),
			dto.getPassword(),
			dto.getName(),
			PhoneConverter.toEntityList(dto.getPhones())
		);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(UserConverter.toSignUpResponseDTO(loggedUser));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestHeader("Authorization") String authHeader) {
		AuthService.LoggedUser loggedUser = authService.login(authHeader);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(UserConverter.toLoginResponseDTO(loggedUser));
	}
}
