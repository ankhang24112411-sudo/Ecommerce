package com.khang.backendecommerce.infrastructure.configuration;

import com.khang.backendecommerce.newstruc.domain.authentication.service.JwtService;
import com.khang.backendecommerce.newstruc.service.UserService;
import com.khang.backendecommerce.infrastructure.util.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j(topic = "CUSTOMIZE - FILTER")
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor

public class PreFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("--------PreFilter--------");
        log.info("Request : method={}, uri={}, servletPath={}", request.getMethod(), request.getRequestURI(), request.getServletPath());
        final String authorization = request.getHeader(AUTHORIZATION);
        log.info("Authorization: {}" ,authorization);
        if(StringUtils.isBlank(authorization) || !authorization.startsWith(BEARER_PREFIX)){
            filterChain.doFilter(request,response);
            return;

        }
        final String token = authorization.substring(BEARER_PREFIX.length());
        final String userName = jwtService.extractUsername(token , TokenType.ACCESS_TOKEN);

        if(StringUtils.isNotEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);
            if (jwtService.isValid(token ,TokenType.ACCESS_TOKEN, userDetails)){
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null , userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        }
        log.info("Token :", token);

        filterChain.doFilter(request,response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean isCreateUserRequest =
                "POST".equalsIgnoreCase(request.getMethod())
                        && (
                        "/user".equals(request.getServletPath())
                                || "/user/".equals(request.getServletPath())
                );

        return isCreateUserRequest;
    }
}
