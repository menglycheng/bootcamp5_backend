package com.checkme.CheckMe.user.controller;

import com.checkme.CheckMe.auth.dto.SuccessResponse;
import com.checkme.CheckMe.email.EmailSenderService;
import com.checkme.CheckMe.user.dto.*;
import com.checkme.CheckMe.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailSenderService.class);

    // Get profile
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
        LOGGER.info("Get Profile");
        return ResponseEntity.ok(userService.getProfile());
    }

    // Get user by username
    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getUserByUsername(@PathVariable("username") String username) {
        LOGGER.info("Get User By Username");
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // Update profile
    @PatchMapping(value = "/profile", consumes = "multipart/form-data")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @Valid @RequestBody @ModelAttribute UpdateProfileDTO request
    ) {
        LOGGER.info("Update Profile");
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    // Activate user
    @PostMapping ("/activate")
    public ResponseEntity<SuccessResponse> enableUser(
            @Valid @RequestBody EnableUserRequest request
    ) {
        LOGGER.info("Enable User");
        return ResponseEntity.ok(userService.enableUser(request.getEmail()));
    }

    // Deactivate user
    @PostMapping ("/deactivate")
    public ResponseEntity<SuccessResponse> disableUser() {
        LOGGER.info("Disable User");
        return ResponseEntity.ok(userService.disableUser());
    }

    // Update organizer
    @PatchMapping ("/organizer")
    public ResponseEntity<UpdateOrganizerProfileDTO> updateOrganizer(
            @Valid @RequestBody UpdateOrganizerProfileDTO request
    ) {
        LOGGER.info("Update Organizer");
        return ResponseEntity.ok(userService.updateOrganizerProfile(request));
    }

    // Become organizer
    @PostMapping ("/organizer")
    public ResponseEntity<OrganizerDTO> becomeOrganizer(
            @Valid @RequestBody OrganizerDTO request
    ) {
        LOGGER.info("Become Organizer");
        return ResponseEntity.ok(userService.becomeOrganizer(request));
    }
}
