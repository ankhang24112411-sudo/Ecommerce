package com.khang.backendecommerce.infrastructure.common.entity.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String version;
}
