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

import java.io.File;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDTO {
    private String name;
    private String firstName;
    private String lastName;
    private String username;
    private String description;
    private MultipartFile profilePicture;
    @Enumerated(EnumType.STRING)
    private Affiliation affiliation;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
