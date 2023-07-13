package com.checkme.CheckMe.user.dto;

import com.checkme.CheckMe.user.entity.Affiliation;
import com.checkme.CheckMe.user.entity.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileResponse {
    private String name;
    private String firstName;
    private String lastName;
    private String username;
    private String description;
    private String profilePicture;
    @Enumerated(EnumType.STRING)
    private Affiliation affiliation;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
