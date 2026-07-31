package com.khang.backendecommerce.domain.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String version;
}
