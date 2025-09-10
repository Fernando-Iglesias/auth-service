package com.iglesiasfernando.auth_service.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "phones")
public class Phone {
	/**
	 * The unique identifier for the phone entry.
	 */
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
	private UUID id;

	/**
	 * The {@link User} associated with this phone entry.
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	/**
	 * The phone number.
	 */
	private String number;

	/**
	 * The city code associated with the phone number.
	 */
	private String cityCode;

	/**
	 * The country code associated with the phone number.
	 */
	private String countryCode;

	public Phone() {}

	public Phone(String number, String cityCode, String countryCode) {
		this.number = number;
		this.cityCode = cityCode;
		this.countryCode = countryCode;
	}

	public UUID getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Phone phone = (Phone) o;

		return number != null ? number.equals(phone.number) : phone.number == null &&
			cityCode != null ? cityCode.equals(phone.cityCode) : phone.cityCode == null &&
			countryCode != null ? countryCode.equals(phone.countryCode) : phone.countryCode == null;

	}
}
