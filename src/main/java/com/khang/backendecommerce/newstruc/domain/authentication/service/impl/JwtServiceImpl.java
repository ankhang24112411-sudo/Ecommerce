package com.khang.backendecommerce.newstruc.domain.authentication.service.impl;

import com.khang.backendecommerce.newstruc.domain.authentication.service.JwtService;
import com.khang.backendecommerce.infrastructure.util.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.khang.backendecommerce.infrastructure.util.TokenType.*;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.expiryHour}")
    private long expiryHour;

    @Value("${jwt.expiryDay}")
    private long expiryDay;


    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.resetToken}")
    private String resetToken;

    @Value("${jwt.refreshKey}")
    private String refreshToken;

    @Override
    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return generateRefreshToken(new HashMap<>(), user);
    }


    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN))
                .compact();
    }

    @Override
    public boolean isValid(String token, TokenType type, UserDetails userDetails) {
        final String username = extractUsername(token, type);
        return username.equals(userDetails.getUsername()) && !
                isTokenExpired(token, type);
    }

    @Override
    public String generateResetToken(UserDetails user) {
        return generateResetToken(new HashMap<>(), user);
    }

    private String generateResetToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey(RESET_TOKEN))
                .compact();
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryHour))
                .signWith(getKey(ACCESS_TOKEN))
                .compact();
    }

    private SecretKey getKey(TokenType type) {
        byte[] keyBytes;
        if (ACCESS_TOKEN.equals(type)) {
            keyBytes = Decoders.BASE64.decode(accessKey);
        } else if (REFRESH_TOKEN.equals(type)) {
            keyBytes = Decoders.BASE64.decode(refreshToken);
        } else {
            keyBytes = Decoders.BASE64URL.decode(resetToken);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimResolver.apply(claims);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parser()
                .verifyWith(getKey(type))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

}
