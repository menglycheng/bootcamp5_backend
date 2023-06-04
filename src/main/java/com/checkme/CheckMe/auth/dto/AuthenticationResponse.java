package com.checkme.CheckMe.auth.dto;

import com.checkme.CheckMe.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
