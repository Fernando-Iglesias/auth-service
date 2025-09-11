package com.iglesiasfernando.auth_service.dtos;

import java.time.LocalDateTime;

public class ErrorDTO {
	private final LocalDateTime timestamp;
	private final int codigo;
	private final String detail;

	public ErrorDTO(LocalDateTime timestamp, int codigo, String detail) {
		this.timestamp = timestamp;
		this.codigo = codigo;
		this.detail = detail;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDetail() {
		return detail;
	}
}
