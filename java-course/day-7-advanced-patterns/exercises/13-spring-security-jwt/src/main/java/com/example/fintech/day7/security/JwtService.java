package com.example.fintech.day7.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Issues and validates HS256-signed JWTs.
 *
 * <p>The signing key is hardcoded for the exercise. In production, load it from
 * configuration (environment variable, vault, secret manager) — never commit secrets.
 */
@Service
public class JwtService {

    private static final Duration EXPIRY = Duration.ofHours(1);

    // HS256 requires a key of at least 32 bytes.
    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            "change-me-in-production-32-bytes-min!".getBytes(StandardCharsets.UTF_8));

    public String generateToken(String username, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(EXPIRY)))
                .signWith(KEY)
                .compact();
    }

    /**
     * Verifies the signature and expiry, then returns the claims.
     * Throws {@link io.jsonwebtoken.JwtException} if the token is invalid.
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
