package com.ecommerce.project.auth.service;

import com.ecommerce.project.auth.dto.AuthResponse;
import com.ecommerce.project.auth.dto.LoginRequest;
import com.ecommerce.project.auth.dto.RegisterRequest;
import com.ecommerce.project.exception.custom.ApiException;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.model.enums.Roles;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.jwt.JwtService;
import com.ecommerce.project.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Purpose
 * → Performs login authentication and user registration.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs() / 1000);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByName(request.getUsername())) {
            throw new ApiException("Username '%s' is already taken".formatted(request.getUsername()));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email '%s' is already registered".formatted(request.getEmail()));
        }

        Role userRole = roleRepository.findByRoles(Roles.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default role ROLE_USER not found in database"));

        User user = new User();
        user.setName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        UserDetails userDetails = new UserPrincipal(user);
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs() / 1000);
    }

}
