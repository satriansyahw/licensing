package com.belajar.task_api.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {
    private final String jwtSecret = "iniSecretKeyYangSangatPanjangDanRahasia1234567890";
    private final int jwtExpirationMs = 86400000; // 24 jam
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 Jam
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken1(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims) // Versi 0.11.x menggunakan 'setClaims' untuk Map
                .setSubject(username) // Menggunakan prefix 'set'
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token JWT kedaluwarsa: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT tidak didukung: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token JWT malformed: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Signature JWT tidak cocok: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Claims JWT kosong: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Gagal memvalidasi token JWT: ", e);
        }
        return false;
    }

}