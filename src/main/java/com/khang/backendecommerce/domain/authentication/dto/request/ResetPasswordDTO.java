package com.khang.backendecommerce.domain.authentication.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordDTO implements Serializable {
    private String secretKey;
    private String password;
    private String confirmPassword;

}
