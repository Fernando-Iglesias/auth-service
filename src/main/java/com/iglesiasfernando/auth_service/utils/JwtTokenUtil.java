package com.iglesiasfernando.auth_service.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

	@Value("${jwt.auth.secret}")
	private String secretKey;

	@Value("${jwt.auth.expiration-ms}")
	private long expirationMs;

	public String generateAuthenticationToken(String email) {
		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}
}
