package com.khang.backendecommerce.api;

import com.khang.backendecommerce.domain.authentication.service.AuthenticationService;
import com.khang.backendecommerce.domain.user.repository.UserRepository;
import com.khang.backendecommerce.domain.authentication.dto.response.TokenResponse;
import com.khang.backendecommerce.domain.authentication.dto.request.SignInRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@Tag(name ="Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserRepository userRepo;
    private final AuthenticationService authenticationService;
    @PostMapping("/access")
    public ResponseEntity<TokenResponse> login (@RequestBody SignInRequest request){
        return new ResponseEntity<>( authenticationService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh (HttpServletRequest request){
        return new ResponseEntity<>( authenticationService.refresh(request), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (HttpServletRequest request){
        return new ResponseEntity<>( authenticationService.logout(request), HttpStatus.OK);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email){
        return new ResponseEntity<>(authenticationService.forgotPassword(email), HttpStatus.OK);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String secretKey){
        return new ResponseEntity<>(authenticationService.resetPassword(secretKey), HttpStatus.OK);
    }


}
