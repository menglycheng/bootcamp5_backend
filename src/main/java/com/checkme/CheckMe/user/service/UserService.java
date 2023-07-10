package com.checkme.CheckMe.user.service;

import com.checkme.CheckMe.auth.dto.SuccessResponse;
import com.checkme.CheckMe.auth.entity.ConfirmationToken;
import com.checkme.CheckMe.auth.repository.ConfirmationTokenRepository;
import com.checkme.CheckMe.email.EmailEnableUserTemplate;
import com.checkme.CheckMe.email.EmailSenderService;
import com.checkme.CheckMe.exception.BadRequestException;
import com.checkme.CheckMe.exception.ResourceNotFoundException;
import com.checkme.CheckMe.user.dto.OrganizerDTO;
import com.checkme.CheckMe.user.dto.UpdateOrganizerProfileDTO;
import com.checkme.CheckMe.user.dto.UpdateProfileDTO;
import com.checkme.CheckMe.user.dto.UserProfileResponse;
import com.checkme.CheckMe.user.entity.Organizer;
import com.checkme.CheckMe.user.entity.User;
import com.checkme.CheckMe.user.repository.OrganizerRepository;
import com.checkme.CheckMe.user.repository.RefreshTokenRepository;
import com.checkme.CheckMe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OrganizerRepository organizerRepository;

    public void enableUserByEmail(String email) {
        userRepository.enableUser(email);
    }


    public void resetPassword(String email, String encode) {
        userRepository.resetPassword(email, encode);
    }

    // Get profile of current user
    public UserProfileResponse getProfile() {
        // Get user from security context
        var userPrinciple = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        var user = userRepository.findById(userPrinciple.getId()).orElseThrow();
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUniqueUsername())
                .email(user.getEmail())
                .description(user.getDescription())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .affiliation(user.getAffiliation())
                .gender(user.getGender())
                .organizer(user.getOrganizer())
                .build();
    }

    // Get user by username
    public UserProfileResponse getUserByUsername(String username) {
        // Get user from database
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUniqueUsername())
                .email(user.getEmail())
                .description(user.getDescription())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .affiliation(user.getAffiliation())
                .organizer(user.getOrganizer())
                .build();
    }


    // update user profile
    public UpdateProfileDTO updateProfile(UpdateProfileDTO request) {
        // Get user from security context
        var userPrinciple = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        var user = userRepository.findById(userPrinciple.getId()).orElseThrow();
        // Update user information if it is not null
        user.setName(request.getName() != null ? request.getName() : user.getName());
        user.setFirstName(request.getFirstName() != null ? request.getFirstName() : user.getFirstName());
        user.setLastName(request.getLastName() != null ? request.getLastName() : user.getLastName());
        user.setDescription(request.getDescription() != null ? request.getDescription() : user.getDescription());
        user.setAffiliation(request.getAffiliation() != null ? request.getAffiliation() : user.getAffiliation());
        user.setGender(request.getGender() !=null ? request.getGender() : user.getGender());
        user.setImageUrl(request.getImageUrl() != null ? request.getImageUrl() : user.getImageUrl());
        // If user update username, check if it is unique
        if (request.getUsername() != null) {
            // Check if username is unique
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("Username is already taken");
            }
            user.setUsername(request.getUsername());
        }
        // Save user
        userRepository.save(user);
        return UpdateProfileDTO.builder()
                .name(user.getName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUniqueUsername())
                .description(user.getDescription())
                .imageUrl(user.getImageUrl())
                .affiliation(user.getAffiliation())
                .gender(user.getGender())
                .build();
    }

    // activate user
    public SuccessResponse enableUser(String email) {
        // Find user by email
        var user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        // If user is already enabled
        if (user.isEnabled()) {
            throw new BadRequestException("User already enabled");
        }
        // Generate confirmation token
        String token = UUID.randomUUID().toString();

        // Build confirmation token object
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(java.time.LocalDateTime.now())
                .expiresAt(java.time.LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        // Save confirmation token to database
        confirmationTokenRepository.save(confirmationToken);

        // Send confirmation email
        String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        emailSenderService.send(
                user.getEmail(),
                EmailEnableUserTemplate.enableUserEmailTemplate(user.getFirstName(), link),
                "Activate your account"
        );

        return SuccessResponse.builder()
                .message("Enable user email has been sent")
                .build();
    }

    // deactivate user
    public SuccessResponse disableUser() {
        // Get user from security context
        var userPrinciple = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        var user = userRepository.findById(userPrinciple.getId()).orElseThrow();
        // If user is already disabled
        if (!user.isEnabled()) {
            throw new BadRequestException("User already disabled");
        }
        // Disable user
        userRepository.disableUserByEmail(user.getEmail());
        // Delete all refresh token from database
        refreshTokenRepository.deleteAllByUserId(user.getId());
        return SuccessResponse.builder()
                .message("Disable user successfully")
                .build();
    }

    // Update user organizer profile information
    public UpdateOrganizerProfileDTO updateOrganizerProfile(UpdateOrganizerProfileDTO request) {
        // Get user from security context
        var userPrinciple = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        var user = userRepository.findById(userPrinciple.getId()).orElseThrow();
        // Get organizer from user
        var organizer = user.getOrganizer();
        // If user is not an organizer
        if (organizer == null) {
            throw new BadRequestException("User is not an organizer");
        }
        // Update organizer information if it is not null
        organizer.setName(request.getName() != null ? request.getName() : organizer.getName());
        organizer.setEmail(request.getEmail() != null ? request.getEmail() : organizer.getEmail());
        organizer.setFacebook_url(request.getFacebook_url() != null ? request.getFacebook_url() : organizer.getFacebook_url());
        organizer.setInstagram_url(request.getInstagram_url() != null ? request.getInstagram_url() : organizer.getInstagram_url());
        // Save organizer
        organizerRepository.save(organizer);
        return UpdateOrganizerProfileDTO.builder()
                .name(organizer.getName())
                .email(organizer.getEmail())
                .facebook_url(organizer.getFacebook_url())
                .instagram_url(organizer.getInstagram_url())
                .build();
    }

    // Become an organizer service
    public OrganizerDTO becomeOrganizer(OrganizerDTO request) {
        // Get user from security context
        var userPrinciple = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        var user = userRepository.findById(userPrinciple.getId()).orElseThrow();
        // If user is already an organizer
        if (user.getOrganizer() != null) {
            throw new BadRequestException("User is already an organizer");
        }
        // Create organizer
        var organizer = Organizer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .facebook_url(request.getFacebook_url())
                .instagram_url(request.getInstagram_url())
                .build();
        // Save organizer
        organizerRepository.save(organizer);
        // Update user
        user.setOrganizer(organizer);
        // Save user
        userRepository.save(user);
        return OrganizerDTO.builder()
                .name(organizer.getName())
                .email(organizer.getEmail())
                .facebook_url(organizer.getFacebook_url())
                .instagram_url(organizer.getInstagram_url())
                .build();
    }

    // Generate unique username
    // Example: if username "john.doe" already exists, then generate "john.doe.1"
    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String username = baseUsername;
        while (userRepository.existsByUsername(username)) {
            int randomNum = ThreadLocalRandom.current().nextInt(1, 1000);
            username = baseUsername + "." + randomNum;
        }
        return username;
    }

    // Generate unique username from social account login
    public String generateUniqueUsername(String firstName, String lastName, String name) {
        // if firstName and lastName is null, then return null
        if (firstName == null || lastName == null) {
            String baseUsername = name.toLowerCase().replaceAll("\\s", ".");
            String username = baseUsername;
            while (userRepository.existsByUsername(username)) {
                int randomNum = ThreadLocalRandom.current().nextInt(1, 1000);
                username = baseUsername + "." + randomNum;
            }
            return username;
        }
        return generateUniqueUsername(firstName, lastName);
    }
}
