package com.checkme.CheckMe.auth.service;

import com.checkme.CheckMe.auth.dto.*;
import com.checkme.CheckMe.auth.entity.ForgotPasswordToken;
import com.checkme.CheckMe.auth.repository.ConfirmationTokenRepository;
import com.checkme.CheckMe.auth.repository.ForgotPasswordTokenRepository;
import com.checkme.CheckMe.email.EmailConfirmationTemplate;
import com.checkme.CheckMe.email.EmailForgotPasswordTemplate;
import com.checkme.CheckMe.exception.BadRequestException;
import com.checkme.CheckMe.exception.EmailAlreadyExistsException;
import com.checkme.CheckMe.exception.ResourceNotFoundException;
import com.checkme.CheckMe.auth.entity.ConfirmationToken;
import com.checkme.CheckMe.email.EmailSenderService;
import com.checkme.CheckMe.jwt.JwtService;
import com.checkme.CheckMe.jwt.JwtUtil;
import com.checkme.CheckMe.user.dto.RefreshTokenRequest;
import com.checkme.CheckMe.user.dto.RefreshTokenResponse;
import com.checkme.CheckMe.user.dto.UserProfileResponse;
import com.checkme.CheckMe.user.entity.*;
import com.checkme.CheckMe.user.repository.RefreshTokenRepository;
import com.checkme.CheckMe.user.repository.UserRepository;
import com.checkme.CheckMe.user.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailSenderService emailSenderService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ForgotPasswordTokenService forgotPasswordTokenService;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    @Value("${app.auth.jwt.refresh-token-rotation}")
    private boolean JWT_REFRESH_TOKEN_ROTATION;
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    // Register user
    public SuccessResponse register(@NotNull RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            LOGGER.error("Email already exists");
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Generate unique username
        String username = userService.generateUniqueUsername(request.getFirstName(), request.getLastName());

        // Create user object
        var user = User.builder()
                .name(request.getFirstName() + " " + request.getLastName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(username)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .provider(AuthProvider.email)
                .role(Role.USER)
                .build();

        // If organizer is in the request, add it to the user
        if (request.getOrganizer() != null) {
            Organizer organizer = request.getOrganizer();

            user.setOrganizer(organizer);
            user.setRole(Role.ORGANIZER);
        }

        // Save user to database
        userRepository.save(user);

        // Generate Confirmation token
        String token = UUID.randomUUID().toString();
        var confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(java.time.LocalDateTime.now())
                .expiresAt(java.time.LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        // Save confirmation token to database
        confirmationTokenRepository.save(confirmationToken);

        // Send confirmation token to user email
        String link = "http://localhost:3000/confirm?token=" + token;
        emailSenderService.send(
                request.getEmail(),
                EmailConfirmationTemplate.confirmEmailTemplate(user.getFirstName(), link),
                "Confirm your email"
        );

        return SuccessResponse.builder()
                .message("User registered successfully. Please check your email to confirm registration.")
                .build();
    }

    // Authenticate user
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Authenticate user with email and password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            if (e.getMessage().equals("Bad credentials")) {
                LOGGER.error("Incorrect username or password");
                throw new BadRequestException("Incorrect username or password");
            }
            LOGGER.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }

        // Find user by email
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        return generateAuthenticationResponse(user);
    }

    // Generate AuthenticationResponse
    public AuthenticationResponse generateAuthenticationResponse(User user) {
        // Generate JWT token
        JwtToken jwtToken = jwtService.generateJwtToken(user);

        // Generate userProfileResponse
        UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUniqueUsername())
                .email(user.getEmail())
                .description(user.getDescription())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole())
                .affiliation(user.getAffiliation())
                .gender(user.getGender())
                .organizer(user.getOrganizer())
                .build();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .user(userProfileResponse)
                .build();
    }

    // Logout user
    public void logout(@NotNull RefreshTokenRequest request) {
        // Get refresh token id from request
        var refreshToken = request.getRefreshToken();
        // Get refresh token id from refresh token string
        var refreshTokenId = jwtUtil.extractRefreshTokenId(refreshToken);
        // If refresh token id is valid and refresh token exists in database
        if (jwtUtil.isRefreshTokenValid(refreshToken) && refreshTokenRepository.existsById(refreshTokenId)) {
            // Delete refresh token from database
            refreshTokenRepository.deleteById(refreshTokenId);
        } else {
            throw new BadRequestException("Refresh token is invalid");
        }
    }

    // Logout all user from all devices
    public void logoutAll(@NotNull RefreshTokenRequest request) {
        // Get refresh token id from request
        var refreshToken = request.getRefreshToken();
        // Get refresh token id from refresh token string
        var refreshTokenId = jwtUtil.extractRefreshTokenId(refreshToken);
        // If refresh token id is valid and refresh token exists in database
        if (jwtUtil.isRefreshTokenValid(refreshToken) && refreshTokenRepository.existsById(refreshTokenId)) {
            // Get user from user id in refresh token
            var user = userRepository.findUserByEmail(jwtUtil.extractUsernameRefreshToken(refreshToken))
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", jwtUtil.extractUsernameRefreshToken(refreshToken)));
            // Delete all refresh token from database
            refreshTokenRepository.deleteAllByUserId(user.getId());
        } else {
            throw new BadRequestException("Refresh token is invalid");
        }
    }

    // Refresh token
    public RefreshTokenResponse refreshToken(@NotNull RefreshTokenRequest request) {
        // Get refresh token id from request
        var refreshToken = request.getRefreshToken();
        // Get refresh token id from refresh token string
        var refreshTokenId = jwtUtil.extractRefreshTokenId(refreshToken);
        // If refresh token id is valid and refresh token exists in database
        if (jwtUtil.isRefreshTokenValid(refreshToken) && refreshTokenRepository.existsById(refreshTokenId)) {
            // Get user from user id in refresh token
            var user = userRepository.findUserByEmail(jwtUtil.extractUsernameRefreshToken(refreshToken))
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", jwtUtil.extractUsernameRefreshToken(refreshToken)));
            // Generate access token
            var accessToken = jwtUtil.generateAccessToken(user);

            // If refresh token rotation is enabled
            if (JWT_REFRESH_TOKEN_ROTATION) {
                // Delete refresh token from database
                refreshTokenRepository.deleteById(refreshTokenId);
                // Build refresh token object
                RefreshToken newRefreshToken = RefreshToken.builder()
                        .user(user)
                        .build();
                // Save refresh token to database
                refreshTokenRepository.save(newRefreshToken);
                // Generate extraClaims for refresh token
                Map<String, Object> extraClaims = Map.of(
                        "refreshTokenId", newRefreshToken.getId()
                );
                // Generate refresh token with extraClaims
                var newRefreshTokenString = jwtUtil.generateRefreshToken(extraClaims, user);

                return RefreshTokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshTokenString)
                        .build();
            } else {
                return RefreshTokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        } else {
            throw new BadRequestException("Refresh token is invalid");
        }
    }

    // Confirm user email
    public SuccessResponse confirmToken(String token) {
        // Find confirmation token by token
        var confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Confirmation token not found"));
        // If confirmation token is already confirmed
        if (confirmationToken.getConfirmedAt() != null) {
            throw new BadRequestException("Email already confirmed");
        }
        // If confirmation token is expired
        if (confirmationToken.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new BadRequestException("Confirmation token expired");
        }
        // Set confirmation token confirmedAt
        confirmationTokenService.setConfirmedAt(token);
        // Enable user
        userService.enableUserByEmail(confirmationToken.getUser().getEmail());
        return SuccessResponse.builder()
                .message("Email verified successfully. Now you can login to your account")
                .build();
    }

    public SuccessResponse forgotPassword(String email) {
        // Find user by email
        var user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        // If user is not enabled
        if (!user.isEnabled()) {
            throw new BadRequestException("User is not activated");
        }
        // Generate forgot password token
        String token = UUID.randomUUID().toString();

        // Build forgot password token object
        ForgotPasswordToken forgotPasswordToken = ForgotPasswordToken.builder()
                .token(token)
                .createdAt(java.time.LocalDateTime.now())
                .expiresAt(java.time.LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        // Save forgot password token to database
        forgotPasswordTokenRepository.save(forgotPasswordToken);

        // Send forgot password email
        String link = "http://localhost:3000/reset-password?token=" + token;
        emailSenderService.send(
                user.getEmail(),
                EmailForgotPasswordTemplate.forgotPasswordEmailTemplate(user.getFirstName(), link),
                "Forgot Password"
        );

        return SuccessResponse.builder()
                .message("Forgot password email has been sent")
                .build();
    }

    public SuccessResponse resetPassword(@NotNull ResetPasswordRequest request) {
        // Find forgot password token by token
        var forgotPasswordToken = forgotPasswordTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Forgot password token not found"));
        // If forgot password token is already confirmed
        if (forgotPasswordToken.getConfirmedAt() != null) {
            throw new BadRequestException("Forgot password token already confirmed");
        }
        // If forgot password token is expired
        if (forgotPasswordToken.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new BadRequestException("Forgot password token expired");
        }
        // Set forgot password token confirmedAt
        forgotPasswordTokenService.setConfirmedAt(request.getToken());
        // Set new password
        userService.resetPassword(
                forgotPasswordToken.getUser().getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        return SuccessResponse.builder()
                .message("Password reset successfully with the new password")
                .build();
    }

    public SuccessResponse changePassword(ChangePasswordRequest request) {
        // Get user from security context
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // If old password is not correct
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is not correct");
        }

        // Set new password
        userService.resetPassword(
                user.getEmail(),
                passwordEncoder.encode(request.getNewPassword())
        );

        return SuccessResponse.builder()
                .message("Password changed successfully")
                .build();
    }
}
