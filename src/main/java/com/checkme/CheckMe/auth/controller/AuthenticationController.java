package com.checkme.CheckMe.auth.controller;

import com.checkme.CheckMe.auth.dto.*;
import com.checkme.CheckMe.auth.service.AuthenticationService;
import com.checkme.CheckMe.auth.dto.ConfirmationTokenRequest;
import com.checkme.CheckMe.email.EmailSenderService;
import com.checkme.CheckMe.user.dto.RefreshTokenRequest;
import com.checkme.CheckMe.user.dto.RefreshTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailSenderService.class);

    // Register user
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        LOGGER.info("Register User");
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
    }

    // Confirm token from email
    @PostMapping("/confirm")
    public ResponseEntity<SuccessResponse> confirm(
            @Valid @RequestBody ConfirmationTokenRequest request
            ) {
        LOGGER.info("Confirm Token");
        return ResponseEntity.ok(authenticationService.confirmToken(request.getToken()));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        LOGGER.info("Login User");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    // Logout current device
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        LOGGER.info("Logout User");
        authenticationService.logout(request);
        return ResponseEntity.ok("Logout successfully");
    }

    // Logout all devices
    @PostMapping("/logout-all")
    public ResponseEntity<String> logoutAll(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        LOGGER.info("Logout All User");
        authenticationService.logoutAll(request);
        return ResponseEntity.ok("Logout successfully");
    }

    // Refresh token
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        LOGGER.info("Refresh Token");
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    // Forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        LOGGER.info("Forgot Password");

        return ResponseEntity.ok(authenticationService.forgotPassword(request.getEmail()));
    }

    // Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        LOGGER.info("Reset Password");
        return ResponseEntity.ok(authenticationService.resetPassword(request));
    }

    // Change password (after login)
    @PostMapping("/change-password")
    public ResponseEntity<SuccessResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        LOGGER.info("Change Password");
        return ResponseEntity.ok(authenticationService.changePassword(request));
    }
}
