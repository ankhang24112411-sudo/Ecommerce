package com.khang.backendecommerce.infrastructure.configuration;

import com.khang.backendecommerce.newstruc.entity.UserEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.security.access.AccessDeniedException;

@Component
public class CurrentUserProvider {
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Usinvalider is");
        }
        Object userObj = authentication.getPrincipal();
        if (userObj instanceof UserEntity currentUser) {
            return currentUser;
        }
        throw new IllegalStateException(
                "Not a valid user"
        );
    }

    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
