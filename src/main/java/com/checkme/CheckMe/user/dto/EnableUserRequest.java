package com.checkme.CheckMe.user.dto;

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
public class EnableUserRequest {
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String email;
}
