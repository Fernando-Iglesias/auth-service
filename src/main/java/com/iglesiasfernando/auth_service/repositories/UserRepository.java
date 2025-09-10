package com.iglesiasfernando.auth_service.repositories;

import com.iglesiasfernando.auth_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findById(UUID id);
	boolean existsByEmail(String email);
}
