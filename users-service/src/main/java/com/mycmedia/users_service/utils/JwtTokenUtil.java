package com.mycmedia.users_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;
import java.security.Key;


@Component
public class JwtTokenUtil {

    @Value("${security.jwt.secret}")
    private String secretKey;

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000* 60 * 60 *10))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public String generateToken(String username) {
//        System.out.println("Generating token for user: " + username);
//
//        long validity = 3600000;
//
//
//        String compact = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + validity))
//                .signWith(SignatureAlgorithm.HS256, secretKey) // Base64 encoding applied
//                .compact();
//
//        SecretKey key = Jwts.SIG.HS256.key().build();
//
//
//        System.out.println(compact);
//        return compact;
//    }

    // Extract username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim from the token
    public <T> T extractClaim(String token, ClaimExtractor<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.extract(claims);
    }

    // Interface for extracting claims
    public interface ClaimExtractor<T> {
        T extract(Claims claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
//                .verifyWith(getSignKey()) //verifyWith
                .setSigningKey(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validate if the token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    // Get the authentication object from the token
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = extractUsername(token);
        return new UsernamePasswordAuthenticationToken(username, null, null); // Customize authorities/roles if necessary
    }
}
