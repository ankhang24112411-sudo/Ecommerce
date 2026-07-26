package com.khang.backendecommerce.domain.authentication.service;

import com.khang.backendecommerce.domain.authentication.dto.request.SignInRequest;
import com.khang.backendecommerce.domain.authentication.dto.response.TokenResponse;
import com.khang.backendecommerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepo;
    private final AuthenticationManager authenticationManager;
    @Override
    public TokenResponse authenticate(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      var user =  userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Cannot find user"));
        return TokenResponse.builder()
                .accessToken("access-T")
                .refreshToken("refresh-T")
                .userId(user.getId())
                .build();
    }
}
