package com.checkme.CheckMe.auth.repository;

import com.checkme.CheckMe.auth.entity.ForgotPasswordToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, String> {
    @Transactional
    @Query("SELECT f FROM ForgotPasswordToken f WHERE f.token = ?1")
    Optional<ForgotPasswordToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ForgotPasswordToken f " + "SET f.confirmedAt = ?2 " + "WHERE f.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
