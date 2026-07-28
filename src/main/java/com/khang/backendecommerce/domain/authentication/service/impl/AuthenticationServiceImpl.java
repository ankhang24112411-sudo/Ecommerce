package com.khang.backendecommerce.domain.authentication.service.impl;

import com.khang.backendecommerce.domain.authentication.dto.request.SignInRequest;
import com.khang.backendecommerce.domain.authentication.dto.response.TokenResponse;
import com.khang.backendecommerce.domain.authentication.entity.TokenEntity;
import com.khang.backendecommerce.domain.authentication.service.AuthenticationService;
import com.khang.backendecommerce.domain.authentication.service.JwtService;
import com.khang.backendecommerce.domain.authentication.service.TokenService;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.domain.user.repository.UserRepository;
import com.khang.backendecommerce.domain.user.service.UserService;
import com.khang.backendecommerce.infrastructure.exception.InvalidDataException;
import com.khang.backendecommerce.infrastructure.util.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static org.springframework.http.HttpHeaders.REFERER;


import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepo;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final JwtService jwtService;
    @Override
    public TokenResponse authenticate(SignInRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      var user =  userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Cannot find user"));

      String accessToken = jwtService.generateToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);

      tokenService.save(TokenEntity.builder()
              .username(user.getUsername())
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .build());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("x-token");
        if(StringUtils.isBlank(refreshToken)){
            throw new InvalidDataException("Token must be not blank ");
        }
        // extract user from Token
        final String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        System.out.println("userName: " + userName);
        //check it to DBS
        Optional<UserEntity> user = userRepo.findByUsername(userName);

        if(!jwtService.isValid(refreshToken,TokenType.REFRESH_TOKEN, user.get())){
            throw new InvalidDataException("Token is invalid");
        }
        String accessToken = jwtService.generateToken(user.get());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request) {
        final String token = request.getHeader(REFERER);
        if(StringUtils.isBlank(token)){
            throw new InvalidDataException("Token must be not blank ");
        }
        final String userName = jwtService.extractUsername(token,TokenType.ACCESS_TOKEN);
        tokenService.delete(userName);

        return "Log out successful";
    }

    @Override
    public String forgotPassword(String email) {
        log.info("---------- forgotPassword ----------");

        UserEntity user = userService.getByEmail(email);

        String resetToken = jwtService.generateResetToken(user);

        tokenService.save(TokenEntity.builder().username(user.getUsername()).resetToken(resetToken).build());

        // TODO send email to user
        String confirmLink = String.format("curl --location 'http://localhost:80/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("--> confirmLink: {}", confirmLink);

        return resetToken;
    }

    @Override
    public String resetPassword(String secretKey) {
        final String userName = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);
        var user = userService.getByUsername(userName);
        return "Reset";
    }















    private UserEntity validateToken(String token){
        var userName = jwtService.extractUsername(token , TokenType.RESET_TOKEN);

        var user = userService.getByUsername(userName);
        if(!user.isEnabled()){
            throw new InvalidDataException("User not active");

        }
        return user;
    }
}
