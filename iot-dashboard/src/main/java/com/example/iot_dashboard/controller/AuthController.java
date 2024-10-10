package com.example.iot_dashboard.controller;

import com.example.iot_dashboard.dto.AuthRequest;
import com.example.iot_dashboard.dto.AuthResponse;
import com.example.iot_dashboard.model.User;
import com.example.iot_dashboard.service.AuthService;
import com.example.iot_dashboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Call the authentication method from AuthService
            String token = authService.authenticate(request.email(), request.password());

            // Return a successful response with the JWT token
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            // Return 401 Unauthorized in case of authentication failure
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/user-info")
    public ResponseEntity<User> getUserInfo(@RequestParam String token) {
        User user = authService.getUserFromToken(token);
        return ResponseEntity.ok(user);
    }
}
