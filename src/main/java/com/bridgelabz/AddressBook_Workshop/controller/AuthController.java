package com.bridgelabz.AddressBook_Workshop.controller;

import com.bridgelabz.AddressBook_Workshop.dto.UserDTO;
import com.bridgelabz.AddressBook_Workshop.security.JwtUtil;
import com.bridgelabz.AddressBook_Workshop.service.EmailService;
import com.bridgelabz.AddressBook_Workshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private  EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.registerUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> loginRequest) {
        String token = userService.loginUser(loginRequest.get("email"), loginRequest.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }
    // 🔹 Forgot Password - Generate & Send Reset Token
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (!userService.existsByEmail(email)) {
            return "User not found!";
        }

        String resetToken = jwtUtil.generateToken(email);
        emailService.sendResetEmail(email, resetToken);

        return "Password reset link sent!";
    }

    // 🔹 Reset Password - Verify Token & Update Password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestBody Map<String, String> request) {
        if (!jwtUtil.validateToken(token)) {
            return "Invalid or expired token!";
        }

        String email = jwtUtil.extractEmailFromToken(token);
        String newPassword = request.get("password");

        userService.updatePassword(email, newPassword);
        return "Password updated successfully!";
    }
}
