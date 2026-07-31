package com.khang.backendecommerce.domain.authentication.service;

import com.khang.backendecommerce.domain.authentication.entity.TokenEntity;
import com.khang.backendecommerce.domain.authentication.repo.TokenRepository;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepo;

    public String save(TokenEntity token){
        Optional<TokenEntity> optional = tokenRepo.findByUsername(token.getUsername());
        if(optional.isEmpty()){
            tokenRepo.save(token);
            return token.getId();
        }else {
            TokenEntity currentToken = optional.get();
            currentToken.setAccessToken(token.getAccessToken());
            currentToken.setRefreshToken(token.getRefreshToken());
            tokenRepo.save(currentToken);
            return token.getId();
        }
    }
    public void delete(String username){
        TokenEntity token = getByUsername(username);
        tokenRepo.delete(token);
    }
    public TokenEntity getByUsername(String username){
        return tokenRepo.findByUsername(username)
                .orElseThrow(() -> ApplicationErrors.TOKEN_NOT_FOUND);
    }
}
