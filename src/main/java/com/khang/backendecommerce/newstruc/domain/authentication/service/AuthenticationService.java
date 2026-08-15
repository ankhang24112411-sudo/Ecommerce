package com.khang.backendecommerce.newstruc.domain.authentication.service;

import com.khang.backendecommerce.newstruc.domain.authentication.dto.request.ResetPasswordDTO;
import com.khang.backendecommerce.newstruc.domain.authentication.dto.request.SignInRequest;
import com.khang.backendecommerce.newstruc.domain.authentication.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    public TokenResponse authenticate(SignInRequest request);

    public TokenResponse refresh(HttpServletRequest request);

    String logout(HttpServletRequest request);

    String forgotPassword(String email);

    String resetPassword(String secretKey);

    String changePassword(ResetPasswordDTO request);
}
