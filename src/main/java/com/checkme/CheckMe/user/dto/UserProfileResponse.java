package com.checkme.CheckMe.user.dto;

import com.checkme.CheckMe.user.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String description;
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Affiliation affiliation;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Organizer organizer;

}
