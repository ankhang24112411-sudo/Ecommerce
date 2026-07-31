package com.khang.backendecommerce.domain.authentication.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.Platform;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequest implements Serializable {

    @NotBlank(message = "username must be not null")
    private String username;

    @NotBlank(message = "password must be not null")
    private String password;

    private Platform platform;

    private String deviceToken;

    private String version;
}
