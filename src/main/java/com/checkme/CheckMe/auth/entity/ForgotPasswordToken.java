package com.checkme.CheckMe.auth.entity;

import com.checkme.CheckMe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;
}
