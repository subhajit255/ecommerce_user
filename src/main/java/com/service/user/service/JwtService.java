package com.service.user.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey = "cc01c77686ab870abba205a4a9848501b040a4596957dedf93e4cdaf37e16cc7ead3e67b4e3ec129b7de09b9255476fbba5b90cd65a3c160f7916140ace8766280b989eeec1115b9bfb836fae75c4eb718c94920165c928eaa16928dd09cb29b949e30895b5f05d9960f8fa3c2451703f208e36b8ce0809ae5b8b1f383b619d686aa43d9ea6eae064a8248ce12979fb53dc33dfc5bedcc76dc228996ab7fd38a3b547621fc6cfa6b046a04dfde7fe101ddf816afc5f3a34b0fbc8ba743a1791d46345c3af3ce22ab8d7c62dca3b1f91c7287c148083e507aa1457474938fbb6e37b6b562b6a84e46d54974f168e3f4193ca0aa01847ce532ebab7436009b7a2b";

    // generate the token
    public String generateToken(String email, String phone, UUID userId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("phone", phone);
        // use email as the subject of the jwt token and others put into that claims
        return createToken(claims, email);
    }

    // create token
    public String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private SecretKey getKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract username (subject), throws exception if expired or invalid
    public String extractUserName(String token) throws ExpiredJwtException, JwtException {
        return extractClaim(token, Claims::getSubject);
    }
    public UUID extractUserId(String token) throws ExpiredJwtException, JwtException{
        Claims claims = extractAllClaims(token);
        String idAsString = claims.get("id", String.class);
        return UUID.fromString(idAsString);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) throws ExpiredJwtException, JwtException{
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws ExpiredJwtException, JwtException{
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
    // Validate token against user details
    public boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException, JwtException{
        final String username = extractUserName(token);
        System.out.println(username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) throws ExpiredJwtException, JwtException{
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) throws ExpiredJwtException, JwtException{
        return extractClaim(token, Claims::getExpiration);
    }

}
