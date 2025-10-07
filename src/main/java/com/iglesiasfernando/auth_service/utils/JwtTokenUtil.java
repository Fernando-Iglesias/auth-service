package com.iglesiasfernando.auth_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

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


	public String generateToken(String email) {
		return Jwts.builder()
			.setSubject(email)
			.setId(UUID.randomUUID().toString())
			.setIssuedAt(Date.from(Instant.now()))
			.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
			.signWith(signingKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(signingKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public boolean validateToken(String token, String expectedEmail) {
		try {
			final String email = extractUsername(token);
			return (email.equals(expectedEmail) && !isTokenExpired(token));
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}
