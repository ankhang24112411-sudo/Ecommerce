package com.khang.backendecommerce.infrastructure.configuration;

import com.khang.backendecommerce.newstruc.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@RequiredArgsConstructor
public class JpaAuditConfig {
    private final CurrentUserProvider currentUserProvider;

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            try {
                UserEntity user = currentUserProvider.getCurrentUser();
                return Optional.ofNullable(user.getId());
            } catch (Exception e) {
                return Optional.of("SYSTEM");
            }
        };
    }
}