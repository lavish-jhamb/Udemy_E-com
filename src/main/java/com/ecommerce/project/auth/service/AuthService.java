package com.ecommerce.project.auth.service;

import com.ecommerce.project.auth.dto.AuthResponse;
import com.ecommerce.project.auth.dto.LoginRequest;
import com.ecommerce.project.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Purpose
 * → Performs login authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs() / 1000);
    }

}
