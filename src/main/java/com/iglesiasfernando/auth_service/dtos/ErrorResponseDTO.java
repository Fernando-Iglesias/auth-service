package com.iglesiasfernando.auth_service.dtos;

import java.util.List;

public class ErrorResponseDTO {
	private final List<ErrorDTO> error;

	public ErrorResponseDTO(List<ErrorDTO> error) {
		this.error = error;
	}

	public List<ErrorDTO> getError() {
		return error;
	}
}
