package com.hotel.flint.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int experiation;

    public String createMemberToken(String email, Long id){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("id", id);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + experiation*60*1000L))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }

    public String createEmployeeToken(String email, Long id, String department){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("id", id);
        claims.put("department", department);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + experiation*60*1000L))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }
}
