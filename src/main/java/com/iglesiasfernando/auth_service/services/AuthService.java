package com.iglesiasfernando.auth_service.services;

import com.iglesiasfernando.auth_service.entities.Phone;
import com.iglesiasfernando.auth_service.entities.User;

import java.util.List;

public interface AuthService {
	class LoggedUser {
		private User user;
		private String token;

		public LoggedUser(User user, String token) {
			this.user = user;
			this.token = token;
		}

		public User getUser() {
			return user;
		}

		public String getToken() {
			return token;
		}
	}
	LoggedUser signUp(String email, String password, String name, List<Phone> phones);
}
