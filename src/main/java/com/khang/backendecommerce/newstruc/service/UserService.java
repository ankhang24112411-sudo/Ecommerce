package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.request.UserCreationRequest;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    String addUser(UserCreationRequest request);
    UserEntity getByUsername(String username);
    UserEntity getByEmail(String email);

    void saveUser(UserEntity user);
}
