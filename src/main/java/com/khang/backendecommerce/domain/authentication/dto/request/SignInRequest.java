package com.khang.backendecommerce.infrastructure.request;

import com.khang.backendecommerce.infrastructure.common.enums.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class SignInRequest implements Serializable {

    @NotBlank(message = "username must be not null")
    private String username;

    @NotBlank(message = "password must be not null")
    private String password;

    private Platform platform;

    private String deviceToken;

    private String version;
}
