package com.example.hotelmanagertool.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenUtil {


    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(String username, String role, Long userId,String name) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000*60*24);

        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .claim("name",name)
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}