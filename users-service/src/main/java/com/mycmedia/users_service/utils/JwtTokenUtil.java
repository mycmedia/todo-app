package com.mycmedia.users_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private final String secretKey = "f5a8074c32fecff1882b4f6384d407b5a8c2d418dad3832f7bd7f660ef137dc149a34fb0d1167532978e5e8f5d78424fc495af1cb33a2f831c0a6a4be72d67e1d90b76d322b53f129f7d5e8e62b521690cc815d0938871437c8d9d124295d336866b4501cc260452a7f8f6677b064f5d74f41fa0a00dfa7e6d1572ee4d63169b6f90a302934ecbb68eeb578453e212836df64ca07a61b336993b6eb182409daa8574354cb4dd00c32e81f44c089ace761c615807dd2d302696b840ebe48c547af0ae3c178d7b24895ecbcf1747bf6db2a4371ddbee53563f2f51bc4b8cc030b26be0c08cc0dcec03288634255807e99ab3140b5a3e0fef63d465fc11083448d8";

    public String generateToken(String username) {
        System.out.println("Generating token for user: " + username);

        long validity = 3600000;


        String compact = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS256, secretKey) // Base64 encoding applied
                .compact();

        SecretKey key = Jwts.SIG.HS256.key().build();


        System.out.println(compact);
        return compact;
    }

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

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
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
