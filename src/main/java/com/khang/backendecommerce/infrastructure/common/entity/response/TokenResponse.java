package com.khang.backendecommerce.infrastructure.common.response;

import java.io.Serializable;

public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
}
