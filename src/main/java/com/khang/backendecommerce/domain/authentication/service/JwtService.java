package com.khang.backendecommerce.domain.authentication.service;

import com.khang.backendecommerce.infrastructure.util.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
     String generateToken(UserDetails user);

     String extractUsername(String token, TokenType type);


     String generateRefreshToken( UserDetails userDetails);

     boolean isValid(String token,TokenType type , UserDetails user);

     String generateResetToken(UserDetails user);
}
