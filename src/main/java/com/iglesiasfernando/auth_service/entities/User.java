package com.iglesiasfernando.auth_service.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

	/**
	 * The unique identifier for the user.
	 */
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
	private UUID id;

	/**
	 * The email address of the user.
	 * Must be unique across all users.
	 */
	@Column(unique = true)
	private String email;

	/**
	 * The password for the user account.
	 * Should be stored securely (e.g., hashed).
	 */
	private String password;

	/**
	 * The optional name of the user.
	 */
	private String name;

	/**
	 * The list of phone numbers associated with the user.
	 */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Phone> phones = new ArrayList<>();


	/**
	 * Timestamp indicating when the user account was created.
	 */
	@Column(nullable = false)
	private LocalDateTime created;

	/**
	 * Timestamp indicating the last time the user logged in.
	 */
	private LocalDateTime lastLogin;

	/**
	 * Flag indicating whether the user account is active.
	 */
	@Column(nullable = false)
	private boolean isActive;

	public User() {}

	public User(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.created = LocalDateTime.now();
		this.isActive = true;
	}

	public UUID getId() {
		return id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public void addPhone(Phone phone) {
		phones.add(phone);
		phone.setUser(this);
	}

}
