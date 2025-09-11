package com.iglesiasfernando.auth_service.converters;

import com.iglesiasfernando.auth_service.dtos.PhoneDTO;
import com.iglesiasfernando.auth_service.entities.Phone;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneConverter {

	public static PhoneDTO toDTO(Phone phone) {
		if (phone == null) {
			return null;
		}
		return new PhoneDTO(
			phone.getNumber(),
			phone.getCityCode(),
			phone.getCountryCode()
		);
	}

	public static Phone toEntity(PhoneDTO phoneDTO) {
		if (phoneDTO == null) {
			return null;
		}
		Phone phone = new Phone();
		phone.setNumber(phoneDTO.getNumber());
		phone.setCityCode(phoneDTO.getCityCode());
		phone.setCountryCode(phoneDTO.getCountryCode());
		return phone;
	}

	public static List<PhoneDTO> toDTOList(List<Phone> phones) {
		if (phones == null) {
			return Collections.emptyList();
		}
		return phones.stream().map(PhoneConverter::toDTO).collect(Collectors.toList());
	}

	public static List<Phone> toEntityList(List<PhoneDTO> phoneDTOs) {
		if (phoneDTOs == null) {
			return Collections.emptyList();
		}
		return phoneDTOs.stream().map(PhoneConverter::toEntity).collect(Collectors.toList());
	}
}
