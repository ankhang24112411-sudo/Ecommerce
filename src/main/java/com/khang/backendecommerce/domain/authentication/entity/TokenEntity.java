package com.khang.backendecommerce.domain.authentication.entity;

import com.khang.backendecommerce.domain.user.entity.RoleEntity;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "tbl_token")
public class TokenEntity extends AbstractEntity<String> {
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "reset_token")
    private String resetToken;
}
