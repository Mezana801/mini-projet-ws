package com.example.miniprojetgestionscolaire.service;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {
    private String SECRET = "ma-cle-secrete-ma-cle-secrete-ma-cle-secrete";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
