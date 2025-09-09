package com.iglesiasfernando.auth_service.services;

import com.iglesiasfernando.auth_service.entities.Phone;
import com.iglesiasfernando.auth_service.entities.User;

import java.util.List;

public interface AuthService {
	User signUp(String email, String password, String name, List<Phone> phones);
}
