package com.iglesiasfernando.auth_service.converters;

import com.iglesiasfernando.auth_service.dtos.*;
import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.services.AuthService;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

	public static SignUpResponseDTO toSignUpResponseDTO(AuthService.LoggedUser loggedUser) {
		User user = loggedUser.getUser();

		UserDTO userDTO = new UserDTO(
			user.getName(),
			user.getEmail(),
			user.getPassword(),
			PhoneConverter.toDTOList(user.getPhones())
		);

		return new SignUpResponseDTO(
			user.getId(),
			user.getCreated(),
			user.getLastLogin(),
			loggedUser.getToken(),
			user.isActive(),
			userDTO
		);
	}

	public static LoginResponseDTO toLoginResponseDTO(AuthService.LoggedUser loggedUser) {
		User user = loggedUser.getUser();

		return new LoginResponseDTO(
			user.getId(),
			user.getCreated(),
			user.getLastLogin(),
			loggedUser.getToken(),
			user.isActive(),
			user.getName(),
			user.getEmail(),
			user.getPassword(),
			PhoneConverter.toDTOList(user.getPhones())
		);
	}
}
