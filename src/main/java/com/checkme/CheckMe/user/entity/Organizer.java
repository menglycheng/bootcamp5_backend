package com.checkme.CheckMe.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "organizer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "organizer_email_unique",
                        columnNames = "email"
                )
        }
)
public class Organizer {
    @Id
    @SequenceGenerator(
            name = "organizer_sequence",
            sequenceName = "organizer_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "organizer_sequence"
    )
    private Long id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;

    private String facebook_url;
    private String instagram_url;
    private Boolean is_approved = false;
}
