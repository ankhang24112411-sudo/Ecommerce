package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.dto.request.UserCreationRequest;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.projection.NewUserMapper;
import com.khang.backendecommerce.newstruc.repo.UserRepository;
import com.khang.backendecommerce.newstruc.service.UserService;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;

import com.khang.backendecommerce.infrastructure.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

@Slf4j(topic = "USER - SERVICE")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final NewUserMapper userMapper;
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
      ValidationUtils.throwIf(userRepo.existsByEmail(request.getEmail()),() -> ApplicationErrors.EMAIL_NOT_FOUND);
     UserEntity user = userMapper.toUser(request);
     user.setPassword(passwordEncoder.encode(request.getPassword()));
     user.setCreatedBy(user.getId());
     userRepo.save(user);
//     if( user != null){
//         String message = String.format("email=%,id=%s,code=%s", user.getEmail(),user.getId(),"code@123");
//         kafkaTemplate.send("confirm-account-topic", message);
//     }
        return "User account";
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> ApplicationErrors.EMAIL_NOT_FOUND);
    }

    @Override
    public void saveUser(UserEntity user) {
         userRepo.save(user);
    }


}
