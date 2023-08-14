package com.example.beredditclone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtProvider {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    private final String SECRET_KEY = "quyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnvquyetnv";

    public String generateAccessToken(String username) {
        return Jwts.builder().setSubject(String.format("%s", username)).setIssuer("CodeJava").setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION)).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();

    }

}