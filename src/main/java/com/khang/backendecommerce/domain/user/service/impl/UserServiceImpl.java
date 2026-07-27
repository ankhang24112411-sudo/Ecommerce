package com.khang.backendecommerce.domain.user.service;

import com.khang.backendecommerce.domain.user.dto.UserCreationRequest;
import com.khang.backendecommerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER - SERVICE")
public class UserServiceImpl implements UserService{
    private final UserRepository userRepo;
    @Override
    public UserDetailsService userDetailsService() {
        return username ->  userRepo.findByEmail(username).orElseThrow(() ->  new UsernameNotFoundException("Username not found"));

    }

    @Override
    public long addUser(UserCreationRequest request) {
        return 0;
    }
}
