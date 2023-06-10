package com.checkme.CheckMe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizerProfileDTO {
    private String name;
    private String email;
    private String facebook_url;
    private String instagram_url;
}
