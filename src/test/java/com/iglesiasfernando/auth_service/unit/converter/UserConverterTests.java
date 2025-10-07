package com.iglesiasfernando.auth_service.unit.converter;


import com.iglesiasfernando.auth_service.converters.UserConverter;
import com.iglesiasfernando.auth_service.entities.Phone;
import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.services.AuthService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

	private AuthService.LoggedUser createSampleLoggedUser() {
		List<Phone> phones = List.of(
			new Phone("12345678", "11", "54"),
			new Phone("87654321", "22", "45")
		);

		User user = new User("cat@meow.com", "hashedPassword", "Snowball");
		user.setId(UUID.randomUUID());
		user.setPhones(phones);
		user.setCreated(LocalDateTime.of(2025, 10, 6, 12, 30));
		user.setLastLogin(LocalDateTime.of(2025, 10, 6, 13, 0));
		user.setActive(true);

		return new AuthService.LoggedUser(user, "exampleToken");
	}

	@Test
	void toSignUpResponseDTO_mapsAllFieldsCorrectly() {
		AuthService.LoggedUser loggedUser = createSampleLoggedUser();

		var dto = UserConverter.toSignUpResponseDTO(loggedUser);

		assertNotNull(dto);
		assertEquals(loggedUser.getUser().getId(), dto.getId());
		assertEquals(loggedUser.getUser().getCreated(), dto.getCreated());
		assertEquals(loggedUser.getUser().getLastLogin(), dto.getLastLogin());
		assertEquals(loggedUser.getToken(), dto.getToken());
		assertTrue(dto.isActive());

		assertNotNull(dto.getUser());
		assertEquals(loggedUser.getUser().getName(), dto.getUser().getName());
		assertEquals(loggedUser.getUser().getEmail(), dto.getUser().getEmail());
		assertEquals(loggedUser.getUser().getPassword(), dto.getUser().getPassword());

		assertEquals(2, dto.getUser().getPhones().size());
		assertEquals("12345678", dto.getUser().getPhones().get(0).getNumber());
		assertEquals("11", dto.getUser().getPhones().get(0).getCityCode());
		assertEquals("54", dto.getUser().getPhones().get(0).getCountryCode());
		assertEquals("87654321", dto.getUser().getPhones().get(1).getNumber());
		assertEquals("22", dto.getUser().getPhones().get(1).getCityCode());
		assertEquals("45", dto.getUser().getPhones().get(1).getCountryCode());
	}

	@Test
	void toLoginResponseDTO_mapsAllFieldsCorrectly() {
		AuthService.LoggedUser sampleUser = createSampleLoggedUser();

		var dto = UserConverter.toLoginResponseDTO(sampleUser);

		assertNotNull(dto);
		assertEquals(sampleUser.getUser().getId(), dto.getId());
		assertEquals(sampleUser.getUser().getCreated(), dto.getCreated());
		assertEquals(sampleUser.getUser().getLastLogin(), dto.getLastLogin());
		assertEquals(sampleUser.getToken(), dto.getToken());
		assertTrue(dto.isActive());
		assertEquals(sampleUser.getUser().getName(), dto.getName());
		assertEquals(sampleUser.getUser().getEmail(), dto.getEmail());
		assertEquals(sampleUser.getUser().getPassword(), dto.getPassword());

		assertNotNull(dto.getPhones());

		assertEquals(2, dto.getPhones().size());
		assertEquals("12345678", dto.getPhones().get(0).getNumber());
		assertEquals("11", dto.getPhones().get(0).getCityCode());
		assertEquals("54", dto.getPhones().get(0).getCountryCode());
		assertEquals("87654321", dto.getPhones().get(1).getNumber());
		assertEquals("22", dto.getPhones().get(1).getCityCode());
		assertEquals("45", dto.getPhones().get(1).getCountryCode());
	}
}
