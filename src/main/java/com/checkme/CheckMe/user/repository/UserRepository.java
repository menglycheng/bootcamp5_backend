package com.checkme.CheckMe.user.repository;

import com.checkme.CheckMe.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email); // Find user by email

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    // Enable user by email
    @Transactional
    @Modifying
    @Query("UPDATE User u " + "SET u.enabled = TRUE WHERE u.email = ?1")
    void enableUser(String email);

    // Disable user by email
    @Transactional
    @Modifying
    @Query("UPDATE User u " + "SET u.enabled = FALSE WHERE u.email = ?1")
    void disableUserByEmail(String email);

    // Reset password by email
    @Transactional
    @Modifying
    @Query("UPDATE User u " + "SET u.password = ?2 " + "WHERE u.email = ?1")
    void resetPassword(String email, String password);

    Optional<User> findByUsername(String username);
}
