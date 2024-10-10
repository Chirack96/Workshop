package com.example.iot_dashboard.service;

import com.example.iot_dashboard.config.JwtService;
import com.example.iot_dashboard.model.User;
import com.example.iot_dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public String authenticate(String email, String password) {
        try {
            System.out.println("Authenticating email: " + email);

            // Pass email and plain-text password to the AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            System.out.println("Authentication successful.");

            // Retrieve authenticated user
            User authenticatedUser = (User) authentication.getPrincipal();
            System.out.println("Authenticated user: " + authenticatedUser.getEmail());

            // Generate JWT token
            String token = jwtService.generateToken(authenticatedUser.getEmail(),
                    authenticatedUser.getId(),
                    authenticatedUser.getFirstname(),
                    authenticatedUser.getLastname());

            System.out.println("Generated JWT Token: " + token);
            return token;

        } catch (AuthenticationException e) {
            // Log the error and throw a BadCredentialsException if authentication fails
            System.out.println("Authentication failed: " + e.getMessage());
            throw new BadCredentialsException("Invalid email or password", e);
        }
    }


    public User getUserFromToken(String token) {
        String email = jwtService.extractUsername(token);
        return userRepository.findByEmail(email).orElse(null);
    }
}
