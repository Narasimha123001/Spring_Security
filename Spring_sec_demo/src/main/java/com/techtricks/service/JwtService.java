package com.techtricks.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {


    private final String secretKey;

    public JwtService() {
        secretKey = generateSecretKey();
        System.out.println("secret key : " + secretKey);
    }

    // generate the secret key
    public String generateSecretKey() {
        try {
            KeyGenerator keyGen =  KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey  = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key" , e);
        }
    }




    //header.payload.signature // gen thee token
    public String generateToken(String username)  {

        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*3))
                .signWith(getKey() , SignatureAlgorithm.HS256).compact();
    }


    /**
     * Converts the Base64-encoded secret key to a Key object for signing/verifying.
     * @return Key object suitable for HMAC signing
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    private Claims extractAllClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);

    }


    public String extractUserName(String token) {
        return extractClaim(token , Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);

    }




}
