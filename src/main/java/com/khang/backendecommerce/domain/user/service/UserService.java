package com.khang.backendecommerce.domain.user.service;

import com.khang.backendecommerce.domain.user.dto.UserCreationRequest;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    String addUser(UserCreationRequest request);
    UserEntity getByUsername(String username);
    UserEntity getByEmail(String email);

    void saveUser(UserEntity user);
}
