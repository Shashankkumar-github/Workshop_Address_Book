package com.bridgelabz.AddressBook_Workshop.controller;

import com.bridgelabz.AddressBook_Workshop.dto.UserDTO;
import com.bridgelabz.AddressBook_Workshop.security.JwtUtil;
import com.bridgelabz.AddressBook_Workshop.service.EmailService;
import com.bridgelabz.AddressBook_Workshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthController authController;

    private UserDTO validUser;
    private String validEmail;
    private String invalidEmail;
    private String validPassword;
    private String invalidPassword;
    private String validToken;
    private String invalidToken;
    private String newPassword;

    @BeforeEach
    void setUp() {
        validUser = new UserDTO("John Doe", "john@example.com", "password123");
        validEmail = "john@example.com";
        invalidEmail = "invalid@example.com";
        validPassword = "password123";
        invalidPassword = "wrongpassword";
        validToken = "valid-token";
        invalidToken = "invalid-token";
        newPassword = "newPassword123";
    }

    // ✅ Test Login User (Valid & Invalid Scenarios Combined)
    @Test
    void loginUserTest() {
        // Valid login scenario
        when(userService.loginUser(validEmail, validPassword)).thenReturn(validToken);
        ResponseEntity<Map<String, String>> validResponse = authController.loginUser(Map.of("email", validEmail, "password", validPassword));
        assertTrue(validResponse.getBody().get("token").equals(validToken));

        // Invalid login scenario
        when(userService.loginUser(validEmail, invalidPassword)).thenThrow(new RuntimeException("Invalid credentials"));
        Exception invalidLoginException = assertThrows(RuntimeException.class, () ->
                authController.loginUser(Map.of("email", validEmail, "password", invalidPassword))
        );
        assertEquals("Invalid credentials", invalidLoginException.getMessage());
    }

    // ✅ Test Forgot Password (Valid & Invalid Email Combined)
    @Test
    void forgotPasswordTest() {
        // Valid email scenario
        when(userService.existsByEmail(validEmail)).thenReturn(true);
        when(jwtUtil.generateToken(validEmail)).thenReturn(validToken);
        String validResponse = authController.forgotPassword(Map.of("email", validEmail));
        verify(emailService, times(1)).sendResetEmail(validEmail, validToken);
        assertTrue(validResponse.contains("Password reset link sent!"));

        // Invalid email scenario
        when(userService.existsByEmail(invalidEmail)).thenReturn(false);
        String invalidResponse = authController.forgotPassword(Map.of("email", invalidEmail));
        assertFalse(invalidResponse.contains("Password reset link sent!"));
        assertEquals("User not found!", invalidResponse);
    }

    // ✅ Test Reset Password (Valid, Invalid Token & Exception Combined)
    @Test
    void resetPasswordTest() {
        // Valid token scenario
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractEmailFromToken(validToken)).thenReturn(validEmail);
        String validResponse = authController.resetPassword(validToken, Map.of("password", newPassword));
        verify(userService, times(1)).updatePassword(validEmail, newPassword);
        assertTrue(validResponse.contains("Password updated successfully!"));

        // Invalid token scenario
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);
        String invalidResponse = authController.resetPassword(invalidToken, Map.of("password", newPassword));
        assertFalse(invalidResponse.contains("Password updated successfully!"));
        assertEquals("Invalid or expired token!", invalidResponse);

        // Exception scenario (Simulating database failure)
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractEmailFromToken(validToken)).thenReturn(validEmail);
        doThrow(new RuntimeException("Database error")).when(userService).updatePassword(validEmail, newPassword);
        Exception exception = assertThrows(RuntimeException.class, () ->
                authController.resetPassword(validToken, Map.of("password", newPassword))
        );
        assertEquals("Database error", exception.getMessage());
    }
}
