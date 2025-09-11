package com.iglesiasfernando.auth_service.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenUtil {

	@Value("${jwt.auth.secret}")
	private String secretKey;

	@Value("${jwt.auth.expiration-ms}")
	private long expirationMs;

	private SecretKey signingKey;

	@PostConstruct
	public void init() {
		this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String generateAuthenticationToken(String email) {
		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
			.signWith(signingKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
