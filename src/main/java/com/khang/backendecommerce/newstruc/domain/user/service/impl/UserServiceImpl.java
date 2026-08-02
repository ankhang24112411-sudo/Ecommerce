package com.khang.backendecommerce.newstruc.domain.user.service.impl;

import com.khang.backendecommerce.newstruc.domain.user.dto.UserCreationRequest;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.newstruc.domain.user.mapper.UserMapper;
import com.khang.backendecommerce.newstruc.domain.user.repository.UserRepository;
import com.khang.backendecommerce.newstruc.domain.user.service.UserService;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.infrastructure.exception.RessourceAlreadyExistException;
import com.khang.backendecommerce.infrastructure.exception.RessourceNotFoundException;
import com.khang.backendecommerce.infrastructure.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

@Slf4j(topic = "USER - SERVICE")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    // Không inject từ Spring
    private final PasswordEncoder passwordEncoder ;

    @Override
    public UserDetailsService userDetailsService() {
        return username ->  userRepo.findByUsernameWithRoles(username).orElseThrow(() ->  new UsernameNotFoundException("Username not found"));

    }

    @Override
    public String addUser(UserCreationRequest request) {
        log.info("Username nhận được: " + request.getUsername());
      ValidationUtils.throwIf(userRepo.existsByUsername(request.getUsername()),() -> ApplicationErrors.USER_NOT_FOUND);
      ValidationUtils.throwIf(userRepo.existsByEmail(request.getEmail()),() -> ApplicationErrors.EMAIL);
     UserEntity user = userMapper.toUser(request);
     user.setPassword(passwordEncoder.encode(request.getPassword()));
     user.setCreatedBy(user.getId());
     userRepo.save(user);
        return "User account";
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new RessourceNotFoundException("Email not found"));
    }

    @Override
    public void saveUser(UserEntity user) {
         userRepo.save(user);
    }


}
