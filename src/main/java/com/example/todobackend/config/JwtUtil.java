package com.example.todobackend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret:TodoListSecretKeyForJWTToken2026}")
    private String secret;
    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Integer userId, String username) {
        Date exp = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(exp)
                .signWith(getKey())
                .compact();
    }

    public Integer getUserIdFromToken(String token) {
        Claims c = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
        return Integer.parseInt(c.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) { return false; }
    }
}
