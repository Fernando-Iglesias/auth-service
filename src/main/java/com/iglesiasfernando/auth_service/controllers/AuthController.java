package com.iglesiasfernando.auth_service.controllers;

import com.iglesiasfernando.auth_service.converters.PhoneConverter;
import com.iglesiasfernando.auth_service.dtos.LoginRequestDTO;
import com.iglesiasfernando.auth_service.dtos.LoginResponseDTO;
import com.iglesiasfernando.auth_service.dtos.SignUpRequestDTO;
import com.iglesiasfernando.auth_service.dtos.SignUpResponseDTO;
import com.iglesiasfernando.auth_service.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping
	@RequestMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDTO dto) {
		AuthService.LoggedUser loggedUser = authService.signUp(
			dto.getEmail(),
			dto.getPassword(),
			dto.getName(),
			PhoneConverter.toEntityList(dto.getPhones())
		);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(
				new SignUpResponseDTO(
					loggedUser.getUser().getId(),
					loggedUser.getUser().getCreated(),
					loggedUser.getUser().getLastLogin(),
					loggedUser.getToken(),
					loggedUser.getUser().isActive()
				)
			);
	}

	@PostMapping
	@RequestMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
		AuthService.LoggedUser loggedUser = authService.login(
			dto.getEmail(),
			dto.getPassword()
		);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(
				new LoginResponseDTO(
					loggedUser.getUser().getId(),
					loggedUser.getUser().getCreated(),
					loggedUser.getUser().getLastLogin(),
					loggedUser.getToken(),
					loggedUser.getUser().isActive(),
					loggedUser.getUser().getName(),
					loggedUser.getUser().getEmail(),
					dto.getPassword(),
					loggedUser.getUser().getPhones().stream().map(PhoneConverter::toDTO).collect(Collectors.toList())
				)
			);
	}
}
