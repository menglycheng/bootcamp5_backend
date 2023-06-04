package com.checkme.CheckMe.user.service;

import com.checkme.CheckMe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }
}
