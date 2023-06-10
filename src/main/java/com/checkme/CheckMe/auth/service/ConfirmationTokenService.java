package com.checkme.CheckMe.auth.service;

import com.checkme.CheckMe.auth.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(token, java.time.LocalDateTime.now());
    }
}
