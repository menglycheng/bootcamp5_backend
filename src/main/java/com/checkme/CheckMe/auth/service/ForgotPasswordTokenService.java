package com.checkme.CheckMe.auth.service;

import com.checkme.CheckMe.auth.repository.ForgotPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordTokenService {
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    public void setConfirmedAt(String token) {
        forgotPasswordTokenRepository.updateConfirmedAt(token, java.time.LocalDateTime.now());
    }
}
