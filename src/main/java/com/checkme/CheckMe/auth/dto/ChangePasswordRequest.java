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
public class ChangePasswordRequest {
    @NotNull(message = "Old password cannot be null")
    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;

    @NotNull(message = "New password cannot be null")
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
}
