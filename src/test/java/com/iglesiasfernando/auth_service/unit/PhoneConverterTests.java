package com.iglesiasfernando.auth_service.unit;

import com.iglesiasfernando.auth_service.converters.PhoneConverter;
import com.iglesiasfernando.auth_service.dtos.PhoneDTO;
import com.iglesiasfernando.auth_service.entities.Phone;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhoneConverterTests {

	@Test
	void shouldConvertPhoneToPhoneDTO() {
		Phone phone = new Phone("123456789", "01", "34");

		PhoneDTO phoneDTO = PhoneConverter.toDTO(phone);

		assertNotNull(phoneDTO);
		assertEquals(phone.getNumber(), phoneDTO.getNumber());
		assertEquals(phone.getCityCode(), phoneDTO.getCityCode());
		assertEquals(phone.getCountryCode(), phoneDTO.getCountryCode());
	}

	@Test
	void shouldReturnNullWhenConvertingNullPhoneToPhoneDTO() {
		PhoneDTO phoneDTO = PhoneConverter.toDTO(null);

		assertNull(phoneDTO);
	}

	@Test
	void shouldConvertPhoneDTOToPhone() {
		PhoneDTO phoneDTO = new PhoneDTO("123456789", "01", "34");

		Phone phone = PhoneConverter.toEntity(phoneDTO);

		assertNotNull(phone);
		assertEquals(phoneDTO.getNumber(), phone.getNumber());
		assertEquals(phoneDTO.getCityCode(), phone.getCityCode());
		assertEquals(phoneDTO.getCountryCode(), phone.getCountryCode());
	}

	@Test
	void shouldReturnNullWhenConvertingNullPhoneDTOToPhone() {
		Phone phone = PhoneConverter.toEntity(null);

		assertNull(phone);
	}


	@Test
	void shouldConvertListOfPhonesToListOfPhoneDTOs() {
		List<Phone> phones = Arrays.asList(
			new Phone("123456789", "01", "34"),
			new Phone("987654321", "02", "56")
		);

		List<PhoneDTO> phoneDTOs = PhoneConverter.toDTOList(phones);

		assertNotNull(phoneDTOs);
		assertEquals(2, phoneDTOs.size());
		assertEquals(phones.get(0).getNumber(), phoneDTOs.get(0).getNumber());
		assertEquals(phones.get(0).getCityCode(), phoneDTOs.get(0).getCityCode());
		assertEquals(phones.get(0).getCountryCode(), phoneDTOs.get(0).getCountryCode());
		assertEquals(phones.get(1).getNumber(), phoneDTOs.get(1).getNumber());
		assertEquals(phones.get(1).getCityCode(), phoneDTOs.get(1).getCityCode());
		assertEquals(phones.get(1).getCountryCode(), phoneDTOs.get(1).getCountryCode());
	}

	@Test
	void shouldReturnNullWhenConvertingNullListOfPhonesToListOfPhoneDTOs() {
		List<PhoneDTO> phoneDTOs = PhoneConverter.toDTOList(null);

		assertTrue(phoneDTOs.isEmpty());
	}

	@Test
	void shouldConvertListOfPhoneDTOsToListOfPhones() {
		List<PhoneDTO> phoneDTOs = Arrays.asList(
			new PhoneDTO("123456789", "01", "34"),
			new PhoneDTO("987654321", "02", "56")
		);

		List<Phone> phones = PhoneConverter.toEntityList(phoneDTOs);

		assertNotNull(phones);
		assertEquals(2, phones.size());

		assertEquals(phoneDTOs.get(0).getNumber(), phones.get(0).getNumber());
		assertEquals(phoneDTOs.get(0).getCityCode(), phones.get(0).getCityCode());
		assertEquals(phoneDTOs.get(0).getCountryCode(), phones.get(0).getCountryCode());

		assertEquals(phoneDTOs.get(1).getNumber(), phones.get(1).getNumber());
		assertEquals(phoneDTOs.get(1).getCityCode(), phones.get(1).getCityCode());
		assertEquals(phoneDTOs.get(1).getCountryCode(), phones.get(1).getCountryCode());
	}

	@Test
	void shouldReturnNullWhenConvertingNullListOfPhoneDTOsToListOfPhones() {
		List<Phone> phones = PhoneConverter.toEntityList(null);

		assertTrue(phones.isEmpty());
	}
}