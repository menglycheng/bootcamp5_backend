package com.checkme.CheckMe.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_email_unique",
                        columnNames = "email"
                )
        }
)
public class User implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    private String firstName;
    private String lastName;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    private String password;

    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Affiliation affiliation;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizer_id", referencedColumnName = "id", columnDefinition = "BIGINT")
    private Organizer organizer;

    @JsonIgnore
    private boolean enabled;

    public String getUniqueUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); // Return a list of granted authorities
    }

    @Override
    public String getPassword() {
        return password; // Return password
    }

    @Override
    public String getUsername() {
        return email; // Use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return enabled; // Account is enabled or not
    }
}
