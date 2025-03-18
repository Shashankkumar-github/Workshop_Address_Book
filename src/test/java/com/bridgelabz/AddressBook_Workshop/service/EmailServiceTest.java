package com.bridgelabz.AddressBook_Workshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendResetEmail() {
        // ✅ Success Case
        String validRecipient = "test@example.com";
        String token = "dummyToken123";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertDoesNotThrow(() -> emailService.sendResetEmail(validRecipient, token));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        assertTrue(true, "Email should be sent successfully");

        // ❌ Failure Case - Invalid Email
        String invalidRecipient = ""; // Empty email

        Exception invalidEmailException = assertThrows(RuntimeException.class, () -> {
            emailService.sendResetEmail(invalidRecipient, token);
        });
        assertTrue(invalidEmailException.getMessage().contains("Failed to send email"));

        // ❌ Failure Case - Mail Server Down
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Mail server down"));

        Exception serverDownException = assertThrows(RuntimeException.class, () -> {
            emailService.sendResetEmail(validRecipient, token);
        });
        assertFalse(serverDownException.getMessage().isEmpty(), "Exception message should not be empty");
    }
}
