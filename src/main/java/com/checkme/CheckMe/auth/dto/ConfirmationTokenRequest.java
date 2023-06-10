package com.checkme.CheckMe.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationTokenRequest {
    @NotNull(message = "Token cannot be null")
    @NotBlank(message = "Token cannot be blank")
    private String token;
}
