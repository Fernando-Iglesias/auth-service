package com.iglesiasfernando.auth_service.dtos;

import javax.validation.constraints.*;
import java.util.List;

public class SignUpRequestDTO {
	private String name;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Invalid email format")
	private String email;

	@NotNull(message = "Password is mandatory")
	@Size(min = 8, max = 12, message = "Password must be between 8 and 12 characters")
	@Pattern(regexp = "^(?=.*[a-z])(?=(?:[^A-Z]*[A-Z]){1}[^A-Z]*$)(?=(?:[^\\d]*\\d){2}[^\\d]*$)[a-zA-Z\\d]+$", message = "Password must contain lowercase letters, exactly one uppercase letter and exactly two digits")
	private String password;

	private List<PhoneDTO> phones;

	public SignUpRequestDTO() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<PhoneDTO> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneDTO> phones) {
		this.phones = phones;
	}
}
